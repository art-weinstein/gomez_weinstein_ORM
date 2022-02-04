package ormtesting;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ORMConnection {

    private static Connection conn = null;

    public static Connection getConnection() {


        if (conn == null) {

            Properties props = new Properties();
            try {

                Class.forName("org.postgresql.Driver");

                props.load(ORMConnection.class.getClassLoader().getResourceAsStream("connection.properties"));


                String endpoint = props.getProperty("endpoint");

                String url = "jdbc:postgresql://" + endpoint + "/postgres";
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                conn = DriverManager.getConnection(url, username, password);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

        //Test connection
    public static void main(String[] args) {

        Connection conn1 = getConnection();
        Connection conn2 = getConnection();

        System.out.println(conn1);
        System.out.println(conn2);

    }
}
