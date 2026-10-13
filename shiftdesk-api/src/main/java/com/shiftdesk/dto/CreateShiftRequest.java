package com.shiftdesk.dto;

import com.shiftdesk.entity.JobRole;
import java.time.*;

public record CreateShiftRequest(LocalDate date, LocalTime start, LocalTime end, JobRole role) {
}
