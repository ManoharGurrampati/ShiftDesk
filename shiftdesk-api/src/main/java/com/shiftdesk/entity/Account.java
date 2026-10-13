package com.shiftdesk.entity;

import jakarta.persistence.*;

// Login credentials live apart from Employee so the password hash never ends up in an API response
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(unique = true, nullable = false)
	public String email;
	@Column(nullable = false)
	public String passwordHash;
	@OneToOne(optional = false)
	public Employee employee;

	public Account() {
	}

	public Account(String email, String passwordHash, Employee employee) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.employee = employee;
	}
}
