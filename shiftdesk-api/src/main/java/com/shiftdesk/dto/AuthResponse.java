package com.shiftdesk.dto;
import com.shiftdesk.entity.Employee;

public record AuthResponse(String token, Long id, String name, String email, String role, Long storeId, String storeName) {
  public static AuthResponse of(Employee e, String email, String token) {
    return new AuthResponse(token, e.id, e.name, email, e.role.name(),
        e.store == null ? null : e.store.id, e.store == null ? null : e.store.name);
  }
}
