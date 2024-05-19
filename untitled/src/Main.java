import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Project {
    public String project_title;
    public int projectId;
    public int leaderId;

    public static void switchIssueState(String Istate, int issueNum, int userId) {
        String query = "UPDATE issue SET status = ? WHERE id = ? AND user_id = ?";

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/se_schema", "root", "8733");
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, Istate);
            stmt.setInt(2, issueNum);
            stmt.setInt(3, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Issue state updated successfully.");
            } else {
                System.out.println("No issue found with the provided id and user_id.");
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createIssue() {
        // 이슈 생성 코드 추가
    }
}

class MyDashBoard {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/se_schema";
    private static final String USER = "root";
    private static final String PASSWORD = "8733";
    private static final Scanner scanner = new Scanner(System.in);

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public void searchByTitle() {
        System.out.print("Enter the issue title: ");
        String issueTitle = scanner.nextLine();
        String query = "SELECT * FROM issue WHERE title LIKE ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + issueTitle + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") + ", Description: " + rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchByStatus() {
        System.out.print("Enter the issue status: ");
        String istate = scanner.nextLine();
        String query = "SELECT * FROM issue WHERE status LIKE ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + istate + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") + ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchByUserId() {
        System.out.print("Enter the user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        String query = "SELECT * FROM issue WHERE reporter_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") + ", User id: " + rs.getInt("reporter_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        MyDashBoard myDashBoard = new MyDashBoard();
        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    myDashBoard.searchByTitle();
                    break;
                case 2:
                    myDashBoard.searchByStatus();
                    break;
                case 3:
                    myDashBoard.searchByUserId();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Welcome to My Dash Board");
        System.out.println("1. Search by issue title");
        System.out.println("2. Search by issue status");
        System.out.println("3. Search by user ID");
        System.out.print("Enter your choice: ");
    }
}
