package com.napier.sem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 3000);
    }

    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530);

        assertNotNull(emp, "Employee should exist");
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }

    @AfterAll
    static void cleanup()
    {
        if (app != null)
        {
            app.disconnect();
        }
    }

    @Test
    void testGetEmployeeNotFound()
    {
        Employee emp = app.getEmployee(-1);
        assertNull(emp);
    }

    @Test
    void testGetDepartment()
    {
        Department dept = app.getDepartment("Sales");

        assertNotNull(dept);
        assertEquals("d007", dept.dept_no);
        assertEquals("Sales", dept.dept_name);
        assertNotNull(dept.manager);
    }

    @Test
    void testGetDepartmentNotFound()
    {
        Department dept = app.getDepartment("Invalid Department");
        assertNull(dept);
    }

    @Test
    void testGetSalariesByDepartment()
    {
        Department dept = app.getDepartment("Sales");

        assertNotNull(dept);

        var employees = app.getSalariesByDepartment(dept);

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
    }
}