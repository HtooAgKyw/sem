package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Check if an employee was returned
            if (rset.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");

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
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Get employee
        Employee emp = a.getEmployee(255530);

        // Display employee
        a.displayEmployee(emp);

        // Disconnect from database
        a.disconnect();
    }
}