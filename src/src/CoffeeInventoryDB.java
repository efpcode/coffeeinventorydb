
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class CoffeeInventoryDB {

    private static Scanner scanner = new Scanner(System.in);

    private static Connection dbConnect() {
        String url = "jdbc:sqlite:/Users/efpdev/Documents/iths/sqldatabasecourse/labbar/lab3/coffeebeans.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void menuScreen() {
        System.out.println("Pick an option:");
        List<String> options = List.of(
                "Exit Program",
                "Show coffee inventory",
                "Show bean inventory",
                "Add new coffee",
                "Add new bean",
                "Update coffee",
                "Update bean",
                "Search in coffee inventory",
                "Search in bean inventory",
                "Show menu screen"
        );

        for (int i = 1; i < options.size() + 1; i++) {
            System.out.printf("%d. %s\n", i, options.get(i - 1));

        }
    }

    private static void showAllBeans() {
        String sql = "SELECT * FROM bean";
        try {
            Connection conn = dbConnect();
            Statement stmt = conn.createStatement();
            ResultSet beans = stmt.executeQuery(sql);

            System.out.println(" ");
            System.out.println("id Column" + " \t\t|\t\t " + "Bean Name");
            System.out.println("----------------|-------------------");

            while (beans.next()) {
                System.out.println(beans.getInt("beanId") + " \t\t\t\t|\t\t " + beans.getString("beanName"));
            }
            System.out.println(" ");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputNewBean() {
        System.out.println("Enter new bean name:");
        String newBeanName = scanner.nextLine();
        insertNewBean(newBeanName);
        scanner.nextLine();

    }

    private static void insertNewBean(String newBeanName) {
        String sql = "INSERT INTO bean VALUES (?, ?)";
        try {
            Connection conn = dbConnect();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(2, newBeanName);
                pstmt.executeUpdate();
            }catch (SQLException e) {
                System.out.println("Failed to insert new bean");
                System.out.println(e.getMessage());
                conn.rollback();
            }
            conn.commit();

        } catch (SQLException e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        showAllBeans();
        inputNewBean();
        showAllBeans();
    }
}
