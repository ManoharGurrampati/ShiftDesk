package com.shiftdesk.service;
import com.shiftdesk.dto.*;
import com.shiftdesk.entity.*;
import com.shiftdesk.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.List;

@Service
public class ShiftService {
  private final ShiftRepository shifts; private final EmployeeRepository emps;
  public ShiftService(ShiftRepository s, EmployeeRepository e) { shifts = s; emps = e; }

  private ResponseStatusException err(HttpStatus st, String m) { return new ResponseStatusException(st, m); }
  private Employee user(Long id) { return emps.findById(id).orElseThrow(() -> err(HttpStatus.NOT_FOUND, "User not found")); }
  private Employee manager(Long id) {
    Employee m = user(id);
    if (m.role != JobRole.MANAGER) throw err(HttpStatus.FORBIDDEN, "Only managers can do this");
    return m;
  }
  private Shift ownShift(Long shiftId, Employee m) {
    Shift s = shifts.findById(shiftId).orElseThrow(() -> err(HttpStatus.NOT_FOUND, "Shift not found"));
    if (!s.store.id.equals(m.store.id)) throw err(HttpStatus.FORBIDDEN, "Shift belongs to another store");
    return s;
  }
  private void validate(LocalDate d, LocalTime start, LocalTime end, JobRole role) {
    if (d == null || start == null || end == null || role == null || !end.isAfter(start))
      throw err(HttpStatus.BAD_REQUEST, "Date, times and role are required, and end must be after start");
    if (role == JobRole.MANAGER || role == JobRole.ADMIN) throw err(HttpStatus.BAD_REQUEST, "Choose an employee role for the shift");
  }

  public List<Shift> forStore(Long meId, Long storeId) {
    Employee me = user(meId);
    if (me.role != JobRole.ADMIN && (me.store == null || !me.store.id.equals(storeId))) throw err(HttpStatus.FORBIDDEN, "Not your store");
    return shifts.findByStoreIdOrderByShiftDateAscStartTimeAsc(storeId);
  }

  @Transactional
  public Shift create(Long meId, CreateShiftRequest r) {
    Employee m = manager(meId);
    validate(r.date(), r.start(), r.end(), r.role());
    Shift s = new Shift();
    s.store = m.store; s.shiftDate = r.date(); s.startTime = r.start(); s.endTime = r.end(); s.role = r.role();
    return shifts.save(s);
  }

  @Transactional
  public Shift update(Long meId, Long id, UpdateShiftRequest r) {
    Shift s = ownShift(id, manager(meId));
    if (s.claimedBy != null) throw err(HttpStatus.CONFLICT, "Release the shift before editing it");
    validate(r.date(), r.start(), r.end(), r.role());
    s.shiftDate = r.date(); s.startTime = r.start(); s.endTime = r.end(); s.role = r.role();
    return shifts.save(s);
  }

  @Transactional
  public void delete(Long meId, Long id) {
    Shift s = ownShift(id, manager(meId));
    if (s.claimedBy != null) throw err(HttpStatus.CONFLICT, "Release the shift before deleting it");
    shifts.delete(s);
  }

  @Transactional
  public Shift release(Long meId, Long id) {
    Shift s = ownShift(id, manager(meId));
    s.claimedBy = null;
    return shifts.save(s);
  }

  // Optimistic locking (@Version): if two employees claim at once, one commit fails -> 409
  @Transactional
  public Shift claim(Long meId, Long shiftId) {
    Shift s = shifts.findById(shiftId).orElseThrow(() -> err(HttpStatus.NOT_FOUND, "Shift not found"));
    Employee e = user(meId);
    if (s.claimedBy != null) throw err(HttpStatus.CONFLICT, "Shift already claimed");
    if (e.store == null || !e.store.id.equals(s.store.id)) throw err(HttpStatus.FORBIDDEN, "Shift belongs to another store");
    if (e.role != s.role) throw err(HttpStatus.BAD_REQUEST, "Shift requires role " + s.role);
    if (shifts.existsByClaimedByIdAndShiftDateAndStartTimeLessThanAndEndTimeGreaterThan(meId, s.shiftDate, s.endTime, s.startTime))
      throw err(HttpStatus.CONFLICT, "You already have an overlapping shift");
    s.claimedBy = e;
    return shifts.save(s);
  }
}
