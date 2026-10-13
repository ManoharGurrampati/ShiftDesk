package com.shiftdesk.service;
import com.shiftdesk.dto.*;
import com.shiftdesk.entity.*;
import com.shiftdesk.repository.*;
import com.shiftdesk.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
  private final AccountRepository accounts; private final EmployeeRepository emps; private final StoreRepository stores;
  private final PasswordEncoder enc; private final JwtService jwt;
  public AuthService(AccountRepository a, EmployeeRepository e, StoreRepository s, PasswordEncoder enc, JwtService jwt) {
    accounts = a; emps = e; stores = s; this.enc = enc; this.jwt = jwt;
  }
  private static boolean blank(String s) { return s == null || s.isBlank(); }
  private ResponseStatusException err(HttpStatus st, String m) { return new ResponseStatusException(st, m); }

  @Transactional
  public AuthResponse register(RegisterRequest r) {
    if (blank(r.name()) || blank(r.email()) || r.password() == null || r.password().length() < 6)
      throw err(HttpStatus.BAD_REQUEST, "Name, email and a password of at least 6 characters are required");
    if (r.role() == null || r.role() == JobRole.MANAGER || r.role() == JobRole.ADMIN)
      throw err(HttpStatus.BAD_REQUEST, "Choose a valid role");
    String email = r.email().trim().toLowerCase();
    if (accounts.existsByEmail(email)) throw err(HttpStatus.CONFLICT, "Email already registered");
    Store store = r.storeId() == null ? null : stores.findById(r.storeId()).orElse(null);
    if (store == null) throw err(HttpStatus.BAD_REQUEST, "Choose a store");
    Employee e = emps.save(new Employee(r.name().trim(), r.role(), store));
    accounts.save(new Account(email, enc.encode(r.password()), e));
    return AuthResponse.of(e, email, jwt.generate(e.id));
  }

  public AuthResponse login(LoginRequest r) {
    String email = r.email() == null ? "" : r.email().trim().toLowerCase();
    Account a = accounts.findByEmail(email)
        .filter(x -> r.password() != null && enc.matches(r.password(), x.passwordHash))
        .orElseThrow(() -> err(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    return AuthResponse.of(a.employee, a.email, jwt.generate(a.employee.id));
  }
}
