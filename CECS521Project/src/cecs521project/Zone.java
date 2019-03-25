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
public class Zone {
    
    public static void addZone()
    {
        try {
            Connection conn=DatabaseConnection.getConnection();
            File file = new File("zone.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates;
            //truncate existing data
            String query = "Truncate table zone";        
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            
            while (sc.hasNextLine())
            {
                String row = sc.nextLine();
                String [] zoneValues = row.split(",");
                int zoneId= Integer.parseInt(zoneValues[0].trim());
                String zoneName= zoneValues[1];
                int squadNumber= Integer.parseInt(zoneValues[2].trim());
                int numberOfVertices = Integer.parseInt(zoneValues[3].trim());
                coordinates = new StringBuilder();
                coordinates.append("POLYGON((");
                for (int i = 4; i < (numberOfVertices*2)+4; i++) {                   
                    coordinates.append(zoneValues[i]);
                    if(i % 2 == 1)
                        coordinates.append(",");
                }
                coordinates.append(zoneValues[4]);
                coordinates.append(zoneValues[5]);
                coordinates.append("))");
                //System.out.println("Id: "+zoneId+" Name: "+zoneName+" Squad: "+squadNumber+" Vertices: "+numberOfVertices+" Coordinates: "+coordinates);
        
                query = " insert into zone (zoneID, zoneName, squadNumber, area)"
                + " values (?, ?, ?, ST_GEOMFROMTEXT(?));";
                //ST_GEOMFROMTEXT(?,3857));
                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, zoneId);
                preparedStmt.setString (2, zoneName);
                preparedStmt.setInt (3, squadNumber);
                preparedStmt.setString (4, coordinates.toString());
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();
            }
            System.out.println("Zones added to database successfully");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage()+" File not found Error");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }    
    }    
}
