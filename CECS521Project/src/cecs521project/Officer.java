/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs521project;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Ankush
 */
public class Officer {
    
    public static void addOfficer()
    {
        try {
            Connection conn=DatabaseConnection.getConnection();
            File file = new File("officer.txt");
            Scanner sc = new Scanner(file);

            //truncate existing data
            String query = "Truncate table officer";        
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            
            while (sc.hasNextLine())
            {
                String row = sc.nextLine();
                String [] officerValues = row.split(",");
                int officerId= Integer.parseInt(officerValues[0].trim());
                String officerName= officerValues[1];
                int squadNumber= Integer.parseInt(officerValues[2].trim());
                String longitude = officerValues[3];
                String latitude = officerValues[4];
                //System.out.println("Id: "+officerId+" Name: "+officerName+" Squad: "+squadNumber+" long: "+longitude+" lat: "+latitude);
                String location = "POINT("+longitude+" "+latitude+")"; 
                query = " insert into officer (badgeID, officerName, squadNumber, officerLocation)"
                + " values (?, ?, ?, ST_PointFromText(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, officerId);
                preparedStmt.setString (2, officerName);
                preparedStmt.setInt (3, squadNumber);
                preparedStmt.setString (4, location);
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();                
            }
            System.out.println("Officers added to database successfully");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage()+" File not found Error");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }   
    }
    
}
