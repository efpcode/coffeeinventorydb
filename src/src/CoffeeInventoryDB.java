
import java.sql.*;
import java.util.Scanner;

public class CoffeeInventoryDB {
    private static Scanner scanner = new Scanner(System.in);
    private static Connection dbconnect(){
        String url = "jdbc:sqlite:/Users/efpdev/Documents/iths/sqldatabasecourse/labbar/lab3/coffeebeans.db";
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
        return conn;
    }



    public static void main(String[] args) {
        Connection dbc = dbconnect();
    }
}
