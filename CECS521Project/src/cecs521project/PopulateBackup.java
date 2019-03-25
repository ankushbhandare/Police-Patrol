/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cecs521projectPopulate;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
/**
 *
 * @author Ankush
 */
/*
public class Populate {
    
    public static void main(String[] args) {
        // TODO code application logic here
        Connection conn=null;
        DatabaseConnection manager = new DatabaseConnection();
        try {
            conn = manager.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addZone(conn);
        addOfficer(conn);
        addRoute(conn);
        addIncident(conn);
        
    }
    private static void addZone(Connection conn)
    {
        try {
            File file = new File("zone.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates= null;
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

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, zoneId);
                preparedStmt.setString (2, zoneName);
                preparedStmt.setInt   (3, squadNumber);
                preparedStmt.setString (4, coordinates.toString());
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    private static void addOfficer(Connection conn)
    {
        try {
            File file = new File("officer.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates= null;
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
                + " values (?, ?, ?, ST_GEOMFROMTEXT(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, officerId);
                preparedStmt.setString (2, officerName);
                preparedStmt.setInt   (3, squadNumber);
                preparedStmt.setString (4, location);
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }  
    
    private static void addRoute(Connection conn)
    {
        try {
            File file = new File("route.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates= null;
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
                //System.out.println("Id: "+zoneId+" Name: "+zoneName+" Squad: "+squadNumber+" Vertices: "+numberOfVertices+" Coordinates: "+coordinates);
        
                query = " insert into route (routeID, routeLocation)"
                + " values (?, ST_LineStringFromText(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, routeId);
                preparedStmt.setString (2, coordinates.toString());
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    private static void addIncident(Connection conn)
    {
        try {
            File file = new File("officer.txt");
            Scanner sc = new Scanner(file);
            StringBuilder coordinates= null;
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
                + " values (?, ?, ?, ST_GEOMFROMTEXT(?));";

                // create the mysql insert preparedstatement
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt (1, officerId);
                preparedStmt.setString (2, officerName);
                preparedStmt.setInt   (3, squadNumber);
                preparedStmt.setString (4, location);
                //System.out.println(preparedStmt.toString());
                preparedStmt.execute();                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
}
*/