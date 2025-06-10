package com.example.qlnv.service;

import com.example.qlnv.model.Attendance;
import com.example.qlnv.model.Employee;
import com.example.qlnv.repository.AttendanceRepository;
import com.example.qlnv.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public void recordAttendance(Employee employee, LocalDate date, boolean present) {
        if (!attendanceRepository.existsByEmployeeAndDate(employee, date)) {
            Attendance attendance = new Attendance(null, employee, date, present);
            attendanceRepository.save(attendance);
        }
    }

    public long getWorkingDays(Employee employee, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        List<Attendance> attendances = attendanceRepository.findByEmployeeAndDateBetween(employee, start, end);
        return attendances.stream().filter(Attendance::isPresent).count();
    }

    public double calculateSalary(Employee employee, YearMonth month) {
        long workingDays = getWorkingDays(employee, month);
        return (employee.getSalary() / 22) * workingDays; // Giả sử 22 ngày công chuẩn
    }

    public void exportAttendanceReport(HttpServletResponse response, YearMonth month) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Employee ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Working Days");
        header.createCell(3).setCellValue("Calculated Salary");

        List<Employee> employees = employeeRepository.findAll();
        int rowIdx = 1;
        for (Employee emp : employees) {
            long days = getWorkingDays(emp, month);
            double salary = calculateSalary(emp, month);

            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
            row.createCell(2).setCellValue(days);
            row.createCell(3).setCellValue(salary);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}