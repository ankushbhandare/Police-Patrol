/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs521project;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ankush
 */
public class DatabaseConnection {

    private static Connection conn = null;

    public static final Connection getConnection() throws SQLException {

        String[] connectionDetails = new String[5];
        if (conn != null) {
            return conn;
        } else {
            try {
                File file = new File("db.properties");
                Scanner sc = new Scanner(file);
                int i = 0;
                while (sc.hasNextLine()) {
                    connectionDetails[i++] = sc.nextLine();
                }

                String con = ("jdbc:mysql://" + connectionDetails[0] + ":" + connectionDetails[1] + "/" + connectionDetails[2] + "?user=" + connectionDetails[3] + "&password=" + connectionDetails[4] + "&useSSL=false");
                try {
                    conn = DriverManager.getConnection(con);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

                System.out.println(connectionDetails[3] + " successfully connected to database " + connectionDetails[2]);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return conn;
        }
    }
}
