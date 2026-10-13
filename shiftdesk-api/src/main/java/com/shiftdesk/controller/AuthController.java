package com.shiftdesk.controller;
import com.shiftdesk.dto.*;
import com.shiftdesk.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;
  public AuthController(AuthService auth) { this.auth = auth; }

  @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public AuthResponse register(@RequestBody RegisterRequest r) { return auth.register(r); }
  @PostMapping("/login") public AuthResponse login(@RequestBody LoginRequest r) { return auth.login(r); }
}
