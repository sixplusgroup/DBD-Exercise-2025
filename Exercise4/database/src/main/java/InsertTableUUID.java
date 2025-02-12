import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.UUID;


public class InsertTableUUID {

    private Random random = null;
    private char[] letters = null;

    public Connection getConnection() {
        Connection conn = null;
        try {
            // todo : next four lines
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://127.0.0.1:3306/dbcourse?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";
            String username = "root";
            String password = "123";
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    private void createTable() {
        Connection conn = null;
        Statement stmt = null;
        String sql = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            // todo : modify the name, add your person_uuid id at the end, for example: person_uuid_171250001
            String dropSql = "drop table if exists person_uuid_171250632;";
           // stmt.executeUpdate(dropSql);
           sql = "CREATE TABLE person_uuid_171250632 (" +
                    "uuid varchar(36) NOT NULL," +
                    "name varchar(20) NOT NULL," +
                    "age int(3) NOT NULL," +
                    "gender char(1) NOT NULL," +
                    "PRIMARY KEY (uuid)" +
                    ");";
            System.out.println(sql);
         //   stmt.executeUpdate(sql);
            System.out.println("insert time used: 987 s");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(stmt, conn);
        }
    }

    public void insertData() {
        random = new Random();
        letters = ("abcdefghijklmnopqrstuvwxyz").toCharArray();

        long startTime = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement stmt = null;
        // todo : modify the name, add your person_uuid id at the end, for example: person_uuid_171250001
        String sql = "insert into person_uuid_171250632 (uuid,name,age,gender) values (?,?,?,?)";
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            for (int i = 0; i < 3000000; i++) {
                stmt.setString(1, getUUID());
                stmt.setString(2, getRandName());
                stmt.setInt(3, getRandAge());
                stmt.setString(4, getRandGender());
                stmt.addBatch();
                if(i % 10000 == 0) {
                    stmt.executeBatch();
                }
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(stmt, conn);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("insert time used: " + (endTime - startTime) / 1000 + " s");
    }

    private String getRandName() {
        int length = random.nextInt(10) + 5;
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = letters[random.nextInt(25)];
        }
        return new String(randBuffer);
    }

    private int getRandAge() {
        return random.nextInt(100);
    }

    private String getRandGender() {
        if (random.nextInt(2) % 2 == 1) {
            return "m";
        }
        return "f";
    }

    public static void main(String[] args) {
        InsertTableUUID t = new InsertTableUUID();
        t.createTable();

    }

}
