import java.sql.*;

/**
 * Project: Student.java
 * Author: Jason Rossetti
 * Created: 2021-11-11
 */
public class DatabaseConnection {
    private static Connection connection;
    private static ResultSet resultSet;

    public static String connectionString;
    public static String user;
    public static String password;

    public DatabaseConnection(){
        this.connectionString = "jdbc:postgresql://localhost:5432/school";
        this.user = "postgre";
        this.password = "Admin12#";
    }

    public static void connect(){
        try
        {
            connection = DriverManager.getConnection(connectionString, user, password);
            System.out.println(connection);
            if (connection.isValid(5000))
                System.out.println("Connection Succeeded");
        } catch (SQLException e){
            e.getErrorCode();
            System.out.println("Connection failed");
        }
    }

    public void query(String query) {
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.println("Authors table of Books Database");
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.printf("%-8s\t", metaData.getColumnLabel(i));
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    System.out.printf("%-8s\t", resultSet.getObject(i));
                }
                System.out.println();
            }

            } catch (SQLException e){
            System.out.println(e.getErrorCode());

        }
    }
}
