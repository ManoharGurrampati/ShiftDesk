package com.shiftdesk.controller;

import com.shiftdesk.dto.*;
import com.shiftdesk.entity.Shift;
import com.shiftdesk.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShiftController {
	private final ShiftService svc;

	public ShiftController(ShiftService svc) {
		this.svc = svc;
	}

	private static Long me(Authentication a) {
		return (Long) a.getPrincipal();
	}

	@GetMapping("/stores/{id}/shifts")
	public List<Shift> list(Authentication a, @PathVariable Long id) {
		return svc.forStore(me(a), id);
	}

	@PostMapping("/shifts")
	@ResponseStatus(HttpStatus.CREATED)
	public Shift create(Authentication a, @RequestBody CreateShiftRequest r) {
		return svc.create(me(a), r);
	}

	@PutMapping("/shifts/{id}")
	public Shift update(Authentication a, @PathVariable Long id, @RequestBody UpdateShiftRequest r) {
		return svc.update(me(a), id, r);
	}

	@DeleteMapping("/shifts/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(Authentication a, @PathVariable Long id) {
		svc.delete(me(a), id);
	}

	@PostMapping("/shifts/{id}/release")
	public Shift release(Authentication a, @PathVariable Long id) {
		return svc.release(me(a), id);
	}

	@PostMapping("/shifts/{id}/claim")
	public Shift claim(Authentication a, @PathVariable Long id) {
		return svc.claim(me(a), id);
	}
}
