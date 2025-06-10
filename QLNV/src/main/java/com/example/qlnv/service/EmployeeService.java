package com.example.qlnv.service;

import com.example.qlnv.model.Employee;
import com.example.qlnv.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    public void importEmployeesFromExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            Employee employee = new Employee();
            employee.setName(row.getCell(0).getStringCellValue());
            employee.setPosition(row.getCell(1).getStringCellValue());
            employee.setSalary(row.getCell(2).getNumericCellValue());
            employee.setJoinDate(row.getCell(3).getLocalDateTimeCellValue().toLocalDate());
            employeeRepository.save(employee);
        }
        workbook.close();
    }

    public void exportEmployeesToExcel(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Position");
        header.createCell(3).setCellValue("Salary");
        header.createCell(4).setCellValue("Join Date");

        List<Employee> employees = employeeRepository.findAll();
        int rowIdx = 1;
        for (Employee emp : employees) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
            row.createCell(2).setCellValue(emp.getPosition());
            row.createCell(3).setCellValue(emp.getSalary());
            row.createCell(4).setCellValue(emp.getJoinDate().toString());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}