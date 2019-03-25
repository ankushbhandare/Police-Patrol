/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package cecs521project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.util.Scanner;


/**
 *
 * @author Ankush
 */
public class Hw {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String db=args[0];
            Connection conn = DatabaseConnection.getConnection(db);
            switch(args[1]){
                    case "q1":
                        executeRangeQuery(args,conn);
                        break;
                    case "q2":
                        executePointQuery(args,conn);
                        break;
                    case "q3":
                        executefindSquad(args,conn);
                        break;
                    case "q4":
                        executeRouteCoverage(args,conn);
                        break;
                    default:
                        System.out.println("Incorrect query selected");
                        break;
            }
            try{
                conn.close();
                System.out.println("Connection closed Successfully");
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                System.err.println("Incorrect/Insufficient number of parameters provided in command line");
            }
            catch(SQLException ex)
            {
                System.err.println("Connection error");
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private static void executeRangeQuery(String[] args,Connection conn) {
        try {
            StringBuilder coordinates = new StringBuilder();
            coordinates.append("POLYGON((");
            for (int i=3; i<((Integer.parseInt(args[2]))*2)+3;i++) {
                coordinates.append(args[i]+" ");
                if(i % 2 == 0)
                    coordinates.append(",");        
            }
            
            coordinates.append(args[3]+" ");
            coordinates.append(args[4]);
            coordinates.append("))");
            
            String query = " Select incidentID,ST_X(incidentLocation),ST_Y(incidentLocation),incidentType FROM incident WHERE ST_CONTAINS("
                    + "ST_GEOMFROMTEXT(?),incidentLocation) ORDER BY incidentID;";
            
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, coordinates.toString());
            //System.out.println(preparedStmt.toString());
            
            ResultSet rs = preparedStmt.executeQuery();
            boolean rows=false;
            while (rs.next()) {
                    rows=true;
                    String id = rs.getString("incidentID");
                    String y = rs.getString("ST_Y(incidentLocation)");
                    String x = rs.getString("ST_X(incidentLocation)");
                    String type = rs.getString("incidentType").replaceAll("\"", "");

                    System.out.println(id+" "+y+" "+x+" "+type);
                }
            if(rows == false)
            {
                System.out.println("No incidents occured in the given region");
            }
        } 
        catch (SQLException ex) {
            System.err.println("SQL Exception: Incorrect Query to database");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    } 

    private static void executePointQuery(String[] args, Connection conn) {
        try {
            String query = "Select badgeID,Round(ST_Distance_Sphere(officerLocation,incidentLocation)) as distance,officerName"
                    + " from officer,incident where incidentID=? and Round(ST_Distance_Sphere(officerLocation, incidentLocation))<=? "
                    + "order by distance;";
            
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, Integer.parseInt(args[2]));
            preparedStmt.setInt (2, Integer.parseInt(args[3]));
            //System.out.println(preparedStmt.toString());
            
            ResultSet rs = preparedStmt.executeQuery();
            boolean rows=false;
                while (rs.next()) {
                    rows=true;
                    String id = rs.getString("badgeID");
                    String distance = rs.getString("distance").concat("m");
                    String officerName = rs.getString("officerName").replaceAll("\"", "");

                    System.out.println(id+" "+distance+" "+officerName);
                }
            if(rows == false)
            {
                System.out.println("No officer found within "+args[3]+"m from indident "+args[2]);
            }
        } 
        catch (SQLException ex) {
            System.err.println("SQL Exception: Incorrect Query to database");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }

    private static void executefindSquad(String[] args, Connection conn) {
        try {
            String query = "select zoneName from zone,officer where zone.squadNumber=officer.squadNumber "
                    + "and zone.squadNumber=? group by zone.squadNumber;";
            
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, Integer.parseInt(args[2]));
            //System.out.println(preparedStmt.toString());          
            ResultSet rs = preparedStmt.executeQuery();
            boolean rows=false;    
                while (rs.next()) {
                    String zone = rs.getString("zoneName");
                    rows=true;
                    System.out.println("Squad "+args[2]+" is now patrolling:"+zone.replaceAll("\"",""));
                }
            if(rows == false)
            {
                System.out.println("No officer found assigned in squad "+args[2]+" or Squad is not assigned to any zone");
            }
            
            query ="SELECT badgeId,IF(ST_CONTAINS(area,officerLocation)=1,'IN','OUT') as currentLocation,"
                    + "officerName from officer,zone where officer.squadNumber=? and officer.squadNumber=zone.squadNumber;";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, Integer.parseInt(args[2]));
            //System.out.println(preparedStmt.toString());          
            rs = preparedStmt.executeQuery();
                        
            while (rs.next()) {
                String badgeNumber = rs.getString("badgeId");
                String location = rs.getString("currentLocation");
                String name = rs.getString("officerName").replaceAll("\"", "");;
                System.out.println(badgeNumber+" "+location+" "+name);
            }
        } 
        catch (SQLException ex) {
            System.err.println("SQL Exception: Incorrect Query to database");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }

    private static void executeRouteCoverage(String[] args, Connection conn) {
        try {
            String query = "Select zoneId,zoneName from zone where"
                    + " ST_INTERSECTS(area,(Select routeLocation from route where routeId = ?)) "
                    + "order by zoneId;";
            
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, Integer.parseInt(args[2]));
            //System.out.println(preparedStmt.toString());
            
            ResultSet rs = preparedStmt.executeQuery();
            boolean rows=false;
            while (rs.next()) {
                    rows=true;
                    String id = rs.getString("zoneId");
                    String zone = rs.getString("zoneName").replaceAll("\"", "");
                    System.out.println(id + " " + zone);
                }
            if(rows == false)
            {
                System.out.println("Patrol route "+args[2]+" does not exist or pass through any of the current zones");
            }
        } catch (SQLException ex) {
            System.err.println("SQL Exception: Incorrect Query to database");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Incorrect/Insufficient number of parameters provided in command line");
        }
    }
	
    public static class DatabaseConnection {

    private static Connection conn = null;
    public static final Connection getConnection(String filename) throws SQLException {

        String[] connectionDetails = new String[5];
        if (conn != null) {
            return conn;
        } else {
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
                } catch (Exception ex) {
                    
                    System.err.println("Connection Failed. Incorrect details in file found. Database Not Connected");
                    System.exit(0);
                }

                System.out.println(connectionDetails[3] + " successfully connected to database " + connectionDetails[2]);

            } catch (FileNotFoundException ex) {
                System.err.println("Database File Not Found");
            }
            return conn;
        }
    }
    }
}
