
import java.sql.*;
import java.util.ArrayList;
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
        List<String> options = List.of(
                "Exit Program",
                "Show coffee inventory",
                "Show bean inventory",
                "Add new coffee",
                "Add new bean",
                "Update coffee",
                "Update bean",
                "Delete coffee",
                "Delete bean",
                "Search in coffee inventory table",
                "List coffee favorites",
                "Add/Remove a coffee to favorites",
                "Statistics Coffee Inventory",
                "Show menu screen"
        );

        for (int i = 1; i < options.size() + 1; i++) {
            System.out.printf("%d. %s\n", i, options.get(i - 1));

        }
    }

    public static List<Integer> getMaxColumnWidthForCoffeeTable() {
        List<Integer> columnWidths = new ArrayList();
        String sql = "SELECT" +
                " max(length(coffeeName)) AS cName," +
                " max(length(coffeeRoaster)) AS cRoaster," +
                " max(length(coffeeRoastLevel)) AS cRoastLevel," +
                " max(length(coffeeOrigin)) AS cOrigin," +
                " max(length(coffeeArticle)) AS cArticle," +
                " max(length(cast(coffee.coffeePrice AS TEXT))) AS cPrice," +
                " max(length(cast(coffee.coffeeWeight AS TEXT))) AS cWeight," +
                " max(length(cast(bean.beanName AS TEXT))) AS cBeanName" +
                " FROM  coffee, bean";

        try {
            Statement stmt = dbConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                columnWidths.add(rs.getInt("cName"));
                columnWidths.add(rs.getInt("cRoaster"));
                columnWidths.add(rs.getInt("cRoastLevel"));
                columnWidths.add(rs.getInt("cOrigin"));
                columnWidths.add(rs.getInt("cArticle"));
                columnWidths.add(rs.getInt("cPrice"));
                columnWidths.add(rs.getInt("cWeight"));
                columnWidths.add(rs.getInt("cBeanName"));

            }

        } catch (SQLException e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
        System.out.println(columnWidths);
        return columnWidths;
    }

    private static void showAllBeans() {
        String sql = "SELECT * FROM bean";
        try {
            Connection conn = dbConnect();
            Statement stmt = conn.createStatement();
            ResultSet beans = stmt.executeQuery(sql);

            System.out.println(" ");
            System.out.println("Bean ID" + " \t\t|\t\t " + "Bean Name");
            System.out.println("----------------|-------------------");

            while (beans.next()) {
                System.out.println(beans.getInt("beanId") + " \t\t\t\t|\t\t " + beans.getString("beanName"));
            }
            System.out.println(" ");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showAllCoffees() {
        String sql = "SELECT * FROM coffee";
        String spacer = " | ";
        try {
            Connection conn = dbConnect();
            Statement stmt = conn.createStatement();
            ResultSet coffees = stmt.executeQuery(sql);
            while (coffees.next()) {
                System.out.println(
                        coffees.getInt("coffeeId") + spacer +
                                coffees.getString("coffeeName") + spacer +
                                coffees.getString("coffeeRoaster") + spacer +
                                coffees.getString("coffeeRoastLevel") + spacer +
                                coffees.getString("coffeeOrigin") + spacer +
                                coffees.getString("coffeeArticle") + spacer +
                                coffees.getDouble("coffeePrice") + spacer +
                                coffees.getInt("coffeeWeight") + spacer +
                                coffees.getString("coffeeFavorite") + spacer +
                                coffees.getInt("coffeeBeanId")

                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputNewBean() {
        showAllBeans();
        System.out.println("Enter new bean name:");
        String newBeanName = scanner.nextLine();
        if (!newBeanName.isEmpty())
            insertNewBean(newBeanName);
        showAllBeans();

        System.out.println("Press enter to continue:");
        scanner.nextLine();

    }

    private static void inputNewCoffee() {
        showAllCoffees();
        System.out.println("Enter new coffee:");
        String coffeeName = scanner.nextLine();
        System.out.println("Enter new coffee roaster name:");
        String coffeeRoasterName = scanner.nextLine();
        System.out.println("Enter roast level:");
        String roastLevel = scanner.nextLine();
        System.out.println("Enter coffee origin:");
        String coffeeOrigin = scanner.nextLine();
        System.out.println("Enter a coffeePrice: ");
        double coffeePrice = scanner.nextDouble();
        System.out.println("Enter coffee weight in grams:");
        int coffeeWeight = scanner.nextInt();
        System.out.println("Enter beanId for bean table:");
        int beanId = scanner.nextInt();
        insertNewCoffee(coffeeName, coffeeRoasterName, roastLevel, coffeeOrigin, coffeePrice, coffeeWeight, beanId);
        showAllCoffees();
        System.out.println("Press enter to continue:");
        scanner.nextLine();


    }

    private static void inputToUpdateCoffee() {
        showAllCoffees();
        System.out.println("Enter new coffee name:");
        String coffeeName = scanner.nextLine();
        System.out.println("Enter new coffee roast level:");
        String coffeeRoasterName = scanner.nextLine();
        System.out.println("Enter new coffee price:");
        double coffeePrice = scanner.nextDouble();
        System.out.println("Enter new coffee weight:");
        int coffeeWeight = scanner.nextInt();
        System.out.println("Enter coffeeId for coffee table:");
        int coffeeId = scanner.nextInt();
        updateCoffee(coffeeName, coffeeRoasterName, coffeePrice, coffeeWeight, coffeeId);
        showAllCoffees();
        System.out.println("Press enter to continue:");
        scanner.nextLine();

    }

    private static void inputToUpdateBean() {
        showAllBeans();
        System.out.println("Enter new bean name:");
        String newBeanName = scanner.nextLine();
        System.out.println("Enter beanId that corresponds to the bean :");
        int beanId = scanner.nextInt();
        if (!newBeanName.isEmpty())
            updateBean(newBeanName, beanId);
        showAllBeans();
        System.out.println("Press enter to continue:");
        scanner.nextLine();
    }


    private static void inputToDeleteCoffee() {
        showAllCoffees();
        System.out.println("Enter coffeeId to be deleted: ");
        var rmCoffee = scanner.nextInt();
        deleteCoffee(rmCoffee);
        showAllCoffees();
        System.out.println("Press enter to continue");
        scanner.nextLine();

    }

    private static void inputToDeleteBean() {
        showAllBeans();
        System.out.println("Enter beanId to be deleted: ");
        var rmBean = scanner.nextInt();
        deleteBean(rmBean);
        showAllBeans();
        System.out.println("Press enter to continue");
        scanner.nextLine();
    }

    private static void inputCoffeeInventorySearch() {
        System.out.println("Please enter a text snippet to search for: ");
        var pattern = scanner.nextLine();
        if (!pattern.isEmpty()) {
            var newPattern = "%" + pattern + "%";
            coffeeInventorySearch(newPattern);
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    private static void insertNewCoffee(String coffeeName, String coffeeRoasterName, String coffeeRoastLevel, String coffeeOrigin, double coffeePrice, int coffeeWeight, int beanId) {
        String sql = "INSERT INTO coffee (coffeeName, coffeeRoaster, coffeeRoastLevel, coffeeOrigin, coffeePrice, coffeeWeight, coffeeBeanId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = dbConnect();
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, coffeeName);
                stmt.setString(2, coffeeRoasterName);
                stmt.setString(3, coffeeRoastLevel);
                stmt.setString(4, coffeeOrigin);
                stmt.setDouble(5, coffeePrice);
                stmt.setDouble(6, coffeeWeight);
                stmt.setInt(7, beanId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("New coffee could not be inserted undoing entry");
                System.out.println(e.getMessage());
                conn.rollback();
            }

            System.out.println(" Please confirm new coffee insertion default [y]/n: ");
            scanner.nextLine();

            if (!scanner.nextLine().equalsIgnoreCase("n")) {
                conn.commit();
            } else {
                conn.rollback();
            }


        } catch (SQLException e) {
            System.out.print("Could not insert new coffee: ");
            System.out.println(e.getMessage());
        }
    }

    private static void insertNewBean(String newBeanName) {
        String sql = "INSERT INTO bean (beanName) VALUES (?)";
        try {
            Connection conn = dbConnect();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newBeanName);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Failed to insert new bean");
                System.out.println(e.getMessage());
                conn.rollback();
            }

            System.out.println(" Please confirm new coffee insertion default [y]/n: ");

            if (!scanner.nextLine().equalsIgnoreCase("n"))
                conn.commit();
            else
                conn.rollback();

        } catch (SQLException e) {
            System.out.println("Something went wrong");
            System.out.println(e.getMessage());
        }
    }

    private static void updateCoffee(String coffeeName, String coffeeRoastLevel, double coffeePrice, int coffeeWeight, int coffeeId) {
        String sql = "UPDATE coffee SET " +
                "coffeeName = ?, " +
                "coffeeRoastLevel = ?, " +
                "coffeePrice = ?, " +
                "coffeeWeight = ? " +
                "WHERE coffeeId = ?";

        try (
                Connection conn = dbConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            conn.setAutoCommit(false);
            try {
                pstmt.setString(1, coffeeName);
                pstmt.setString(2, coffeeRoastLevel);
                pstmt.setDouble(3, coffeePrice);
                pstmt.setInt(4, coffeeWeight);
                pstmt.setInt(5, coffeeId);
                pstmt.executeUpdate();
                System.out.println("Confirm updated for coffee [y]/n");
                scanner.nextLine();
                if (!scanner.nextLine().equalsIgnoreCase("n")) {
                    conn.commit();
                } else {

                    conn.rollback();
                }

            } catch (SQLException e) {
                System.out.println("Could not update coffee");
                System.out.println(e.getMessage());
                conn.rollback();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateBean(String beanName, int beanId) {
        String sql = "UPDATE bean SET beanName = ? WHERE beanId = ?";
        try (
                Connection conn = dbConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            conn.setAutoCommit(false);
            try {
                pstmt.setString(1, beanName);
                pstmt.setInt(2, beanId);
                pstmt.executeUpdate();
                System.out.println("Confirm updated for coffee [y]/n");
                scanner.nextLine();
                if (scanner.nextLine().equals("n"))
                    conn.rollback();
                else {
                    conn.commit();
                }

            } catch (SQLException e) {
                System.out.println("Could not update Bean");
                System.out.println(e.getMessage());
                conn.rollback();
            }


        } catch (SQLException e) {
            System.out.println("SQL Exception see below: ");
            System.out.println(e.getMessage());
        }
    }


    private static void deleteCoffee(int coffeeId) {
        String sql = "DELETE FROM coffee WHERE coffeeId = ?";

        try (
                Connection conn = dbConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql);

        ) {
            conn.setAutoCommit(false);
            try {
                pstmt.setInt(1, coffeeId);
                pstmt.executeUpdate();
                System.out.println("Confirm deleted for coffee [y]/n");
                scanner.nextLine();
                if (scanner.nextLine().equals("n")) {
                    conn.rollback();
                } else {
                    conn.commit();

                }

            } catch (SQLException e) {
                System.out.println("SQL Exception see below: ");
                System.out.println(e.getMessage());
                conn.rollback();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteBean(int beanId) {
        String sql = "DELETE FROM bean WHERE beanId = ?";

        try (
                Connection conn = dbConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql);

        ) {
            conn.setAutoCommit(false);
            try {
                pstmt.setInt(1, beanId);
                pstmt.executeUpdate();
                System.out.println("Confirm deleted for coffee [y]/n");
                scanner.nextLine();
                if (scanner.nextLine().equals("n")) {
                    conn.rollback();
                } else {
                    conn.commit();

                }

            } catch (SQLException e) {
                System.out.println("SQL Exception see below: ");
                System.out.println(e.getMessage());
                conn.rollback();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void coffeeInventorySearch(String textPattern) {
        String sql = "SELECT coffeeName, coffeeRoaster, coffeeRoastLevel, coffeeOrigin, coffeePrice, beanName FROM coffee " +
                "INNER JOIN bean ON bean.beanId = coffee.coffeeBeanId " +
                "WHERE coffeeName LIKE ? " +
                "OR coffeeRoaster LIKE ? " +
                "OR coffeeRoastLevel LIKE ? " +
                "OR coffeeOrigin LIKE ? " +
                "OR beanName LIKE ? ";

        try {
            Connection conn = dbConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, textPattern);
            pstmt.setString(2, textPattern);
            pstmt.setString(3, textPattern);
            pstmt.setString(4, textPattern);
            pstmt.setString(5, textPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String coffeeName = rs.getString("coffeeName");
                String coffeeRoaster = rs.getString("coffeeRoaster");
                String coffeeRoastLevel = rs.getString("coffeeRoastLevel");
                String coffeeOrigin = rs.getString("coffeeOrigin");
                double coffeePrice = rs.getDouble("coffeePrice");
                String beanName = rs.getString("beanName");
                System.out.printf("%s | %s | %s | %s | %.2f | %s\n", coffeeName, coffeeRoaster, coffeeRoastLevel, coffeeOrigin, coffeePrice, beanName);
            }


        } catch (SQLException e) {
            System.out.println("Could not search for given pattern see error below");
            System.out.println(e.getMessage());
        }
    }

    private static void showFavoritesCoffees() {
        String sql = "SELECT coffeeName, coffeeRoaster, coffeeRoastLevel, coffeeOrigin, coffeePrice, coffeeWeight ,beanName FROM coffee " +
                "INNER JOIN bean ON bean.beanId = coffee.coffeeBeanId " +
                "WHERE coffeeFavorite = true ";

        try (
                Connection conn = dbConnect();
                Statement stmt = conn.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String coffeeName = rs.getString("coffeeName");
                String coffeeRoaster = rs.getString("coffeeRoaster");
                String coffeeRoastLevel = rs.getString("coffeeRoastLevel");
                String coffeeOrigin = rs.getString("coffeeOrigin");
                double coffeePrice = rs.getDouble("coffeePrice");
                int coffeeWeight = rs.getInt("coffeeWeight");
                String beanName = rs.getString("beanName");
                System.out.printf("%s | %s | %s | %s | %.2f | %d | %s\n", coffeeName, coffeeRoaster, coffeeRoastLevel, coffeeOrigin, coffeePrice, coffeeWeight, beanName);
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void inputToFavoriteCoffee(){
        showAllCoffees();
        System.out.println("\n");
        System.out.println("Please pick a coffee ID (Digit) to update/remove as favorite");
        int favoriteCoffee = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Please select if coffee is favorite: y/[n] ");
        String userInput = scanner.nextLine();

        var coffeeIsFavorite = userInput.equals("y");
        updateFavoritesCoffee(favoriteCoffee, coffeeIsFavorite);
        System.out.println("Press enter to continue..");
        showFavoritesCoffees();
        scanner.nextLine();

    }

    private static void updateFavoritesCoffee(int coffeeId, boolean favorite) {
        String sql = "UPDATE coffee SET coffeeFavorite = ? WHERE coffeeId = ?";
        try(
                Connection conn = dbConnect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ){
                    conn.setAutoCommit(false);
                    try{
                        pstmt.setBoolean(1, favorite);
                        pstmt.setInt(2, coffeeId);
                        pstmt.executeUpdate();

                        System.out.println("Confirm updated for coffee [y]/n");
                        if (!scanner.nextLine().equals("n")) {
                            System.out.println("Added/Removed as favorite");
                            conn.commit();
                        }else {
                            System.out.println("Noting added to favorite");
                            conn.rollback();
                        }

                    }catch (SQLException e){
                        System.out.println("SQL Exception see below: ");
                        System.out.println(e.getMessage());
                        conn.rollback();
                    }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void showCoffeePricesGroupedByCountries(){
        String sql = "SELECT coffeeOrigin, coffeePrice, (AVG(coffeePrice/coffeeWeight)*1000.0)  AS avgPricePerKg, " +
                "COUNT(coffeeOrigin) AS countPerCountry, beanName, COUNT(beanName) AS beanPerCountry " +
                "FROM coffee " +
                "INNER JOIN bean b on b.beanId = coffee.coffeeBeanId "+
                "GROUP BY coffeeOrigin ORDER BY avgPricePerKg DESC ";
        try {
            Connection conn = dbConnect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String coffeeOrigin = rs.getString("coffeeOrigin");
                double coffeePrice = rs.getDouble("coffeePrice");
                double avgPricePerKg = rs.getDouble("avgPricePerKg");
                int countPerCountry = rs.getInt("countPerCountry");
                String beanName = rs.getString("beanName");
                int beanCount = rs.getInt("beanPerCountry");
                System.out.printf("%s | %.2f | %.2f | %d | %s | %d \n",coffeeOrigin, coffeePrice, avgPricePerKg,countPerCountry ,beanName, beanCount);
            }

        }catch (SQLException e){
            System.out.println("Something went wrong see error below: ");
            System.out.println(e.getMessage());
        }


    }


    public static void main(String[] args) {
        boolean isLoop = false;
        menuScreen();
        while (!isLoop) {
            System.out.println("\n>>> 1: Exit 14:Menu Screen\n>>> Pick an option below:");
            int userInput = scanner.nextInt();
            scanner.nextLine();
            switch (userInput) {
                case 1 -> isLoop = true;
                case 2 -> showAllCoffees();
                case 3 -> showAllBeans();
                case 4 -> inputNewCoffee();
                case 5 -> inputNewBean();
                case 6 -> inputToUpdateCoffee();
                case 7 -> inputToUpdateBean();
                case 8 -> inputToDeleteCoffee();
                case 9 -> inputToDeleteBean();
                case 10 -> inputCoffeeInventorySearch();
                case 11 -> showFavoritesCoffees();
                case 12 -> inputToFavoriteCoffee();
                case 13 -> showCoffeePricesGroupedByCountries();
                case 14 -> menuScreen();

            }
        }
        System.out.println("Bye bye !");


    }
}
