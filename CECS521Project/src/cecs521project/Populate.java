/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package cecs521project;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ankush
 */
public class Populate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            
            String db=args[0];
            String zoneFile=args[1];
            String officerFile=args[2];
            String routeFile=args[3];
            String incidentFile=args[4];
			
            Connection conn=DatabaseConnection.getConnection(db);
            Zone.addZone(conn,zoneFile);
            Officer.addOfficer(conn,officerFile);
            Route.addRoute(conn,routeFile);
            Incident.addIncident(conn,incidentFile);
			
            try{
                conn.close();
                System.out.println("Connection closed successfully");
            }
            catch(Exception ex){
                System.err.println("Connection Error");
            }
        } 
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
	
public static class Officer {
    
    public static void addOfficer(Connection conn,String filename)
    {
        try {
            //Connection conn=DatabaseConnection.getConnection();
            File file = new File(filename);
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
            System.err.println("File not found :"+filename);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }
    
}

public static class Incident {
    public static void addIncident(Connection conn,String filename)
    {
        try {
            //Connection conn=DatabaseConnection.getConnection();
            File file = new File(filename);
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
            System.err.println("File not found :"+filename);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }   
}

public static class Route {
    public static void addRoute(Connection conn,String filename)
    {
        try {
            //Connection conn=DatabaseConnection.getConnection();
            File file = new File(filename);
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
            System.err.println("File not found :"+filename);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }
    
}
public static class Zone {
    
    public static void addZone(Connection conn,String filename)
    {
        try {
            //Connection conn=DatabaseConnection.getConnection();
            File file = new File(filename);
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
            System.err.println("File not found :"+filename);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }    
}

public static class DatabaseConnection {

    private static Connection conn = null;

    public static Connection getConnection(String filename) throws SQLException {

        String[] connectionDetails = new String[5];
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            int i = 0;
            while (sc.hasNextLine()) {
                connectionDetails[i++] = sc.nextLine();
            }

            String con = ("jdbc:mysql://" + connectionDetails[0] + ":" + connectionDetails[1] + "/" + connectionDetails[2] + "?user=" + connectionDetails[3] + "&password=" + connectionDetails[4] + "&useSSL=false");
            try {
                conn = DriverManager.getConnection(con);
            } catch (SQLException ex) {
                System.err.println("Connection Failed. Incorrect details in file found. Database Not Connected");
                System.exit(0);
            }

            System.out.println(connectionDetails[3] + " successfully connected to database " + connectionDetails[2]);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
        return conn;
        }
    }	
}
