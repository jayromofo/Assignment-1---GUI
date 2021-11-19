/**
 * Project: Assignment 2 - Events and Listeners
 * Author: Jason Rossetti
 * Created: 2021-09-14
 */
/*
    TODO: Some validation and error exception. Pretty sure most of that is done. Might need some for value/type checking the +
        the check boxes
 */
import javax.swing.*;
import java.sql.*;

public class AppGui {

    public static void main(String[] args) {
        // Create the application
        String query = "INSERT INTO javaclass.assignment.students (student_id, first_name, last_name, program) VALUES " +
                "('A00019020', 'Jason', 'Rossetti', 'CPA')";
        String connectionString = "jdbc:postgresql://localhost:5432/javaclass";
        String username = "postgres";
        String password = "admin";

        try {
            Connection connection = DriverManager.getConnection(connectionString, username, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData meta = resultSet.getMetaData();

            if (connection.isValid(5000)){
                System.out.println("Connection Success");
            }
            StudentFrame app = new StudentFrame("Student Records");
            app.setSize(600, 200);
            app.setResizable(false);
            app.setVisible(true);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        // On open make 2 lists. One for original list, one for edited list3

    }
}


