import java.sql.*;
import java.util.Scanner;
import java.util.Random;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Online_Reservation_System{

    private static final int min = 1000;
    private static final int max = 9999;

    public static class user{
        private String username;
        private String password;

        Scanner sc = new Scanner(System.in);

        public user(){
        }

        public String getUsername(){
            System.out.println("Enter username : ");
            username = sc.nextLine();
            return username;
        }

        public String getPassword(){
            System.out.println("Enter password : ");
            password = sc.nextLine();
            return password;
        }
    }

    public static class PnrRecord{
        private int pnrNumber;
        private String passengerName;
        private String trainNumber;
        private String classType;
        private String journeyDate;
        private String from;
        private String to;

        Scanner sc = new Scanner(System.in);

        public int getPnrNumber(){
            Random random = new Random();
            pnrNumber = random.nextInt(max) + min;
            return pnrNumber;
        }

        public String getPassengerName(){
            System.out.println("Enter the passenger name : ");
            passengerName = sc.nextLine();
            return passengerName;
        }

        public String getTrainNumber(){
            System.out.println("Enter the train number : ");
            trainNumber = sc.nextLine();
            return trainNumber;
        }

        public String getClassType(){
            System.out.println("Enter the class type : ");
            classType = sc.nextLine();
            return classType;
        }

        public String getJourneyDate(){
            System.out.println("Enter the journey date as 'YYYY-MM-DD' format : ");
            journeyDate = sc.nextLine();
            return journeyDate;
        }

        public String getFrom(){
            System.out.println("Enter the starting place : ");
            from = sc.nextLine();
            return from;
        }

        public String getTo(){
            System.out.println("Enter the destination place : ");
            to = sc.nextLine();
            return to;
        }
    }


    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        user u1 = new user();
        String username = u1.getUsername();
        String password = u1.getPassword();

        String url = "jdbc:mysql://localhost:3306/work";//database name
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            try(Connection connection = DriverManager.getConnection(url, username, password)){
                System.out.println("User Connection Granted.\n");
                while(true){
                    String insertQuery = "INSERT INTO reservation1 VALUES (?, ?, ?, ?, ?, ?, ?)";
                    String deleteQuery = "DELETE FROM reservation1 WHERE pnr_number = ?";
                    String showQuery = "SELECT * FROM reservation1";

                    System.out.println("Enter the choice: ");
                    System.out.println("1. Insert Record");
                    System.out.println("2. Delete Record");
                    System.out.println("3. Show All Records");
                    System.out.println("4. Exit");
                    int choice = sc.nextInt();

                    if (choice == 1) {
                        PnrRecord p1 = new PnrRecord();
                        int pnrNumber = p1.getPnrNumber();
                        String passengerName = p1.getPassengerName();
                        String trainNumber = p1.getTrainNumber();
                        String classType = p1.getClassType();
                        String journeyDate = p1.getJourneyDate();
                        String from = p1.getFrom();
                        String to = p1.getTo();

                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setInt(1, pnrNumber);
                            preparedStatement.setString(2, passengerName);
                            preparedStatement.setString(3, trainNumber);
                            preparedStatement.setString(4, classType);
                            preparedStatement.setString(5, journeyDate);
                            preparedStatement.setString(6, from);
                            preparedStatement.setString(7, to);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Record added successfully");
                            } else {
                                System.out.println("No records were added");
                            }
                        }
                        catch (SQLException e) {
                            System.err.println("SQLException: " + e.getMessage());
                        }

                        } else if (choice == 2) {
                            System.out.println("Enter the PNR number you want to delete the record");
                            int pnrNumber = sc.nextInt();
                            sc.nextLine(); // consume newline
                            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                            preparedStatement.setInt(1, pnrNumber);
                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Record deleted successfully");
                            } else {
                                System.out.println("No records were deleted");
                            }
                        } catch (SQLException e) {
                            System.err.println(e.getMessage());
                        }
                        }   

                        else if (choice == 3) {
                            try (PreparedStatement preparedStatement = connection.prepareStatement(showQuery);
                             ResultSet resultSet = preparedStatement.executeQuery()) {
                            System.out.println("All records printing \n");
                            while (resultSet.next()) {
                                String pnrNumber = resultSet.getString("pnr_number");
                                String passengerName = resultSet.getString("passenger_Name");
                                String trainNumber = resultSet.getString("train_number");
                                String classType = resultSet.getString("class_type");
                                String journeyDate = resultSet.getString("journey_date");
                                String from = resultSet.getString("from_location");
                                String to = resultSet.getString("to_location");

                                System.out.println("PNR Number : " + pnrNumber);
                                System.out.println("Passenger name : " + passengerName);
                                System.out.println("Train Number : " + trainNumber);
                                System.out.println("Class type : " + classType);
                                System.out.println("Journey Date : " + journeyDate);
                                System.out.println("From Location : " + from);
                                System.out.println("To Location : " + to);

                                System.out.println();
                            }
                            } catch (SQLException e) {
                                System.err.println("SQL Exception : " + e.getMessage());
                            }
                        }
                        else if (choice == 4) {
                            System.out.println("Exiting the program\n");
                            break;
                        } else {
                            System.out.println("Invalid choice entered \n");
                        }
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading JDBC driver " + e.getMessage());
        }
        sc.close();
    }
}