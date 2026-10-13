package com.shiftdesk.repository;
import com.shiftdesk.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.*;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
  List<Shift> findByStoreIdOrderByShiftDateAscStartTimeAsc(Long storeId);
  boolean existsByClaimedByIdAndShiftDateAndStartTimeLessThanAndEndTimeGreaterThan(Long empId, LocalDate d, LocalTime end, LocalTime start);
}
