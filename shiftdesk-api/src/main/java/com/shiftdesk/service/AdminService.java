package com.shiftdesk.service;
import com.shiftdesk.dto.UserSummary;
import com.shiftdesk.entity.*;
import com.shiftdesk.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class AdminService {
  private final AccountRepository accounts; private final EmployeeRepository emps; private final StoreRepository stores;
  public AdminService(AccountRepository a, EmployeeRepository e, StoreRepository s) { accounts = a; emps = e; stores = s; }

  private UserSummary summary(Account a) {
    Employee e = a.employee;
    return new UserSummary(e.id, e.name, a.email, e.role.name(), e.store == null ? null : e.store.name);
  }

  public List<UserSummary> users() { return accounts.findAll().stream().map(this::summary).toList(); }

  @Transactional
  public UserSummary promote(Long employeeId) {
    Employee e = emps.findById(employeeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    if (e.role == JobRole.ADMIN) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admins cannot be changed");
    e.role = JobRole.MANAGER;
    emps.save(e);
    return summary(accounts.findByEmployeeId(employeeId).orElseThrow());
  }

  @Transactional
  public Store createStore(String name) {
    if (name == null || name.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store name is required");
    return stores.save(new Store(name.trim()));
  }
}
