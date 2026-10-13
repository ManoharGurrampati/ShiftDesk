package com.shiftdesk.controller;

import com.shiftdesk.dto.UserSummary;
import com.shiftdesk.entity.Store;
import com.shiftdesk.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	public record StoreRequest(String name) {
	}

	private final AdminService admin;

	public AdminController(AdminService admin) {
		this.admin = admin;
	}

	@GetMapping("/users")
	public List<UserSummary> users() {
		return admin.users();
	}

	@PostMapping("/users/{id}/promote")
	public UserSummary promote(@PathVariable Long id) {
		return admin.promote(id);
	}

	@PostMapping("/stores")
	@ResponseStatus(HttpStatus.CREATED)
	public Store createStore(@RequestBody StoreRequest r) {
		return admin.createStore(r.name());
	}
}
