
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                return;
            }
            catch (SQLException e)
            {
                System.out.println(
                        "Failed to connect to database. Attempt "
                                + (i + 1) + " of " + retries
                );

                System.out.println(e.getMessage());

                if (i < retries - 1)
                {
                    try
                    {
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

        System.out.println("Could not connect to the database.");
    }

    /**
     * Get an employee from the database.
     *
     * @param ID Employee number to search for.
     * @return Employee object, or null if not found.
     */
    public Employee getEmployee(int ID)
    {
        if (con == null)
        {
            System.out.println("No database connection.");
            return null;
        }

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
                        + "WHERE e.emp_no = ? "
                        + "AND t.to_date = '9999-01-01' "
                        + "AND s.to_date = '9999-01-01' "
                        + "AND de.to_date = '9999-01-01' "
                        + "AND dm.to_date = '9999-01-01'";

        try (PreparedStatement stmt = con.prepareStatement(strSelect))
        {
            stmt.setInt(1, ID);

            try (ResultSet rset = stmt.executeQuery())
            {
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

                return null;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Gets all current employees and salaries.
     *
     * @return A list of employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        if (con == null)
        {
            System.out.println("No database connection.");
            return null;
        }

        String strSelect =
                "SELECT employees.emp_no, employees.first_name, "
                        + "employees.last_name, salaries.salary "
                        + "FROM employees "
                        + "JOIN salaries ON employees.emp_no = salaries.emp_no "
                        + "WHERE salaries.to_date = '9999-01-01' "
                        + "ORDER BY employees.emp_no ASC";

        ArrayList<Employee> employees = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(strSelect);
             ResultSet rset = stmt.executeQuery())
        {
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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Gets employees with a given current job title.
     *
     * @param title Job title to search for.
     * @return A list of employees, or null if there is an error.
     */
    public ArrayList<Employee> getEmployeesByTitle(String title)
    {
        if (con == null)
        {
            System.out.println("No database connection.");
            return null;
        }

        String strSelect =
                "SELECT employees.emp_no, employees.first_name, "
                        + "employees.last_name, salaries.salary "
                        + "FROM employees "
                        + "JOIN salaries ON employees.emp_no = salaries.emp_no "
                        + "JOIN titles ON employees.emp_no = titles.emp_no "
                        + "WHERE salaries.to_date = '9999-01-01' "
                        + "AND titles.to_date = '9999-01-01' "
                        + "AND titles.title = ? "
                        + "ORDER BY employees.emp_no ASC";

        ArrayList<Employee> employees = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(strSelect))
        {
            stmt.setString(1, title);

            try (ResultSet rset = stmt.executeQuery())
            {
                while (rset.next())
                {
                    Employee emp = new Employee();

                    emp.emp_no = rset.getInt("emp_no");
                    emp.first_name = rset.getString("first_name");
                    emp.last_name = rset.getString("last_name");
                    emp.salary = rset.getInt("salary");

                    employees.add(emp);
                }
            }

            return employees;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employees by title");
            return null;
        }
    }

    /**
     * Display an employee.
     *
     * @param emp Employee to display.
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
        else
        {
            System.out.println("Employee not found.");
        }
    }

    /**
     * Display a list of employees and their salaries.
     *
     * @param employees Employees to display.
     */
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
            finally
            {
                con = null;
            }
        }
    }

    public static void main(String[] args)
    {
        // Create new application
        App a = new App();

        // Connect to database
        a.connect();

        // Extract employee salary information
        ArrayList<Employee> employees = a.getAllSalaries();

        // Test the size of the returned data
        if (employees != null)
        {
            System.out.println(employees.size());
        }

        // Disconnect from database
        a.disconnect();
    }
}