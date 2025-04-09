package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Session session = factory.openSession();
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.print("Nhập tên sinh viên: ");
            String name = scanner.nextLine();

            // Thêm sinh viên
            session.beginTransaction();
            student s1 = new student(name);
            session.save(s1);
            session.getTransaction().commit();
            System.out.println("Đã lưu sinh viên: " + s1.getName());

            // Lấy danh sách sinh viên
            session = factory.openSession();
            List<student> list = session.createQuery("from student", student.class).list();
            System.out.println("Danh sách sinh viên:");
            for (student s : list) {
                System.out.println("- " + s.getId() + ": " + s.getName());
            }

        } finally {
            session.close();
            factory.close();
            scanner.close();
        }
    }
}
