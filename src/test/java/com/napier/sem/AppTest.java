package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void printSalariesTestNull()
    {
        assertDoesNotThrow(() -> app.printSalaries(null));
    }

    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        assertDoesNotThrow(() -> app.printSalaries(employees));
    }

    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(null);

        assertDoesNotThrow(() -> app.printSalaries(employees));
    }

    @Test
    void printSalariesTestNormal()
    {
        ArrayList<Employee> employees = new ArrayList<>();

        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;

        employees.add(emp);

        assertDoesNotThrow(() -> app.printSalaries(employees));
    }

    @Test
    void displayEmployeeTestNull()
    {
        assertDoesNotThrow(() -> app.displayEmployee(null));
    }

    @Test
    void displayEmployeeTestNormal()
    {
        Employee emp = new Employee();

        emp.emp_no = 10001;
        emp.first_name = "Georgi";
        emp.last_name = "Facello";
        emp.title = "Senior Engineer";
        emp.salary = 88958;

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }

    @Test
    void displayEmployeeTestWithDepartment()
    {
        Employee emp = new Employee();

        emp.emp_no = 10001;
        emp.first_name = "Georgi";
        emp.last_name = "Facello";

        Department dept = new Department();
        dept.dept_no = "d005";
        dept.dept_name = "Development";

        emp.dept = dept;

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }

    @Test
    void displayEmployeeTestWithManager()
    {
        Employee emp = new Employee();
        emp.emp_no = 10001;
        emp.first_name = "Georgi";
        emp.last_name = "Facello";

        Employee manager = new Employee();
        manager.emp_no = 10002;
        manager.first_name = "Bezalel";
        manager.last_name = "Simmel";

        emp.manager = manager;

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }
}