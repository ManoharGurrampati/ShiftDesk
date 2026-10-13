package com.shiftdesk.security;
import com.shiftdesk.repository.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwt; private final EmployeeRepository emps;
  public JwtAuthFilter(JwtService jwt, EmployeeRepository emps) { this.jwt = jwt; this.emps = emps; }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String h = req.getHeader("Authorization");
    if (h != null && h.startsWith("Bearer ")) {
      try {
        // role is read from the database on every request, so a promotion takes effect immediately
        emps.findById(jwt.parse(h.substring(7))).ifPresent(e -> SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(e.id, null, List.of(new SimpleGrantedAuthority("ROLE_" + e.role)))));
      } catch (Exception ignored) { /* invalid or expired token: request stays unauthenticated */ }
    }
    chain.doFilter(req, res);
  }
}
