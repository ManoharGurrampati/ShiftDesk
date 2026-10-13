package com.shiftdesk.dto;
import com.shiftdesk.entity.JobRole;
import java.time.*;
public record UpdateShiftRequest(LocalDate date, LocalTime start, LocalTime end, JobRole role) {}
