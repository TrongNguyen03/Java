package com.example.qlnv.repository;

import com.example.qlnv.model.Attendance;
import com.example.qlnv.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);
    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
}