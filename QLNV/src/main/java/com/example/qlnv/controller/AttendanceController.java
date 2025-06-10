package com.example.qlnv.controller;

import com.example.qlnv.model.Employee;
import com.example.qlnv.service.AttendanceService;
import com.example.qlnv.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    @GetMapping("/export")
    public void exportReport(@RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                             HttpServletResponse response) throws IOException {
        attendanceService.exportAttendanceReport(response, month);
    }

    @PostMapping("/mark")
    public void markAttendance(@RequestParam Long employeeId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam boolean present) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        attendanceService.recordAttendance(employee, date, present);
    }
}
