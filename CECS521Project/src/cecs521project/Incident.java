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
public class Incident {
    public static void addIncident()
    {
        try {
            Connection conn=DatabaseConnection.getConnection();
            File file = new File("incident.txt");
            Scanner sc = new Scanner(file);

            //truncate existing data
            String query = "Truncate table incident";        
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            
            while (sc.hasNextLine())
            {
                String row = sc.nextLine();
                String [] incidentValues = row.split(",");
                int incidentId= Integer.parseInt(incidentValues[0].trim());
                String incidentType= incidentValues[1];
                String longitude = incidentValues[2];
                String latitude = incidentValues[3];
                
                String location = "POINT("+longitude+" "+latitude+")"; 
                query = " insert into incident (incidentID, incidentType, incidentLocation)"
                + " values (?, ?, ST_PointFromText(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, incidentId);
                preparedStmt.setString (2, incidentType);
                preparedStmt.setString (3, location);
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();                
            }
            System.out.println("Incidents added to database successfully");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage()+" File not found Error");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }    
    }
    
}
