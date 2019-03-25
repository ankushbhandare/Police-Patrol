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
public class Route {
    public static void addRoute()
    {
        try {
            Connection conn=DatabaseConnection.getConnection();
            File file = new File("route.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates;
            //truncate existing data
            String query = "Truncate table route";        
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            
            while (sc.hasNextLine())
            {
                String row = sc.nextLine();
                String [] routeValues = row.split(",");
                int routeId= Integer.parseInt(routeValues[0].trim());
                int numberOfVertices = Integer.parseInt(routeValues[1].trim());
                coordinates = new StringBuilder();
                coordinates.append("LINESTRING(");
                for (int i = 2; i < (numberOfVertices*2)+2;) {                   
                    coordinates.append(routeValues[i]);
                    i++;
                    if(i % 2 == 0 && i < (numberOfVertices*2)+2)
                        coordinates.append(",");
                }
                coordinates.append(")");
                        
                query = " insert into route (routeID, routeLocation)"
                + " values (?, ST_LineStringFromText(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, routeId);
                preparedStmt.setString (2, coordinates.toString());
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();
            }
            System.out.println("Routes added to database successfully");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage()+" File not found Error");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }    
    }
    
}
