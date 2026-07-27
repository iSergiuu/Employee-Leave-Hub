package com.leavehub.backend.service;

import com.leavehub.backend.entity.LeaveType;
import com.leavehub.backend.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
}