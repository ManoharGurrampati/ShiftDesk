package com.shiftdesk.dto;
import com.shiftdesk.entity.JobRole;
public record RegisterRequest(String name, String email, String password, Long storeId, JobRole role) {}
