package com.shiftdesk.entity;

import jakarta.persistence.*;

@Entity
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String name;

	public Store() {
	}

	public Store(String name) {
		this.name = name;
	}
}
