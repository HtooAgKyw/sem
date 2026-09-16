package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args)
    {
        Connection con = null;

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

                System.out.println("Successfully connected to database!");
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

        if (con == null)
        {
            System.out.println("Could not connect to database.");
            return;
        }

        try
        {
            con.close();
            System.out.println("Database connection closed.");
        }
        catch (SQLException e)
        {
            System.out.println("Error closing database connection.");
            System.out.println(e.getMessage());
        }
    }
}