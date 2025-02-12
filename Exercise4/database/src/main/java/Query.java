import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Query {

    public static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String DBURL = "jdbc:mysql://127.0.0.1:3306/dbcourse?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";

    public static final String DBUSER = "root";

    public static final String DBPASS = "123";

    public static void main(String[] args) throws Exception{

        Connection conn = null;

        Statement stmt = null;

        ResultSet result = null;

        Class.forName(DBDRIVER);

        conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

        stmt = conn.createStatement();

        long startTime = System.currentTimeMillis();

        //todo: 改表名！加学号！

        String sql = "update person_171250632 set age=20 where name like '%abc%' ";
        stmt.execute(sql);
        System.out.println(sql);

        long endTime = System.currentTimeMillis();
        System.out.println("query time used: " + (endTime - startTime) + " ms");

        if(result != null)
            result.close();
        if(stmt != null)
            stmt.close();
        if(conn != null)
            conn.close();

    }

}
