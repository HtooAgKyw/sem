package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class App
{
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        int retries = 10;

        for (int i = 0; i < retries; i++)
        {
            System.out.println("Connecting to database...");

            try
            {
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException e)
            {
                System.out.println(
                        "Failed to connect to database. Attempt "
                                + (i + 1) + " of " + retries
                );

                System.out.println(e.getMessage());

                try
                {
                    // Wait 5 seconds before trying again
                    Thread.sleep(5000);
                }
                catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /**
     * Get an employee from the database.
     *
     * @param ID employee number to search for
     * @return Employee object, or null if not found
     */
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, "
                            + "t.title, s.salary, d.dept_name, "
                            + "CONCAT(m.first_name, ' ', m.last_name) AS manager "
                            + "FROM employees e "
                            + "JOIN titles t ON e.emp_no = t.emp_no "
                            + "JOIN salaries s ON e.emp_no = s.emp_no "
                            + "JOIN dept_emp de ON e.emp_no = de.emp_no "
                            + "JOIN departments d ON de.dept_no = d.dept_no "
                            + "JOIN dept_manager dm ON d.dept_no = dm.dept_no "
                            + "JOIN employees m ON dm.emp_no = m.emp_no "
                            + "WHERE e.emp_no = " + ID + " "
                            + "AND t.to_date = '9999-01-01' "
                            + "AND s.to_date = '9999-01-01' "
                            + "AND de.to_date = '9999-01-01' "
                            + "AND dm.to_date = '9999-01-01'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Check if an employee was returned
            if (rset.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                emp.dept_name = rset.getString("dept_name");
                emp.manager = rset.getString("manager");

                return emp;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public ArrayList<Employee> getEmployeesByTitle(String title)
    {
        ArrayList<Employee> employees = new ArrayList<>();

        try
        {
            Statement stmt = con.createStatement();

            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, "
                            + "employees.last_name, salaries.salary "
                            + "FROM employees, salaries, titles "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = titles.emp_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "AND titles.to_date = '9999-01-01' "
                            + "AND titles.title = '" + title + "' "
                            + "ORDER BY employees.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);

            while (rset.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");

                employees.add(emp);
            }

            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employees by title");
            return null;
        }
    }


    /**
     * Display an employee.
     *
     * @param emp Employee to display
     */
    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary: " + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n"
            );
        }
    }

    public void displayEmployees(ArrayList<Employee> employees)
    {
        if (employees != null)
        {
            for (Employee emp : employees)
            {
                System.out.println(
                        emp.emp_no + "\t"
                                + emp.first_name + "\t"
                                + emp.last_name + "\t"
                                + emp.salary
                );
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
                System.out.println("Database connection closed");
            }
            catch (SQLException e)
            {
                System.out.println("Error closing connection to database");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        App a = new App();

        a.connect();

        ArrayList<Employee> employees =
                a.getEmployeesByTitle("Engineer");

        a.displayEmployees(employees);

        a.disconnect();
    }
}