package com.shiftdesk.entity;

import jakarta.persistence.*;

@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String name;
	@Enumerated(EnumType.STRING)
	public JobRole role;
	@ManyToOne
	public Store store; // null for ADMIN

	public Employee() {
	}

	public Employee(String name, JobRole role, Store store) {
		this.name = name;
		this.role = role;
		this.store = store;
	}
}
