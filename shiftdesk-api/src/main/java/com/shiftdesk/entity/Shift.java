package com.shiftdesk.entity;

import jakarta.persistence.*;
import java.time.*;

@Entity
public class Shift {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@ManyToOne(optional = false)
	public Store store;
	public LocalDate shiftDate;
	public LocalTime startTime, endTime;
	@Enumerated(EnumType.STRING)
	public JobRole role;
	@ManyToOne
	public Employee claimedBy;
	@Version
	public Long version;
}
