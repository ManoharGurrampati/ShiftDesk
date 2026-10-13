package com.shiftdesk.config;
import com.shiftdesk.entity.*;
import com.shiftdesk.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final StoreRepository stores; private final EmployeeRepository emps; private final AccountRepository accounts; private final PasswordEncoder enc;
  public DataSeeder(StoreRepository s, EmployeeRepository e, AccountRepository a, PasswordEncoder enc) { stores = s; emps = e; accounts = a; this.enc = enc; }

  @Override public void run(String... args) {
    if (stores.count() == 0) { stores.save(new Store("Downtown")); stores.save(new Store("Uptown")); }
    if (!accounts.existsByEmail("admin@shiftdesk.com")) {
      Employee admin = emps.save(new Employee("Admin", JobRole.ADMIN, null));
      accounts.save(new Account("admin@shiftdesk.com", enc.encode("Admin@123"), admin));
    }
  }
}
