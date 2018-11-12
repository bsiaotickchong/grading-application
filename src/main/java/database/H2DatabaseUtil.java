package database;

import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;

public class H2DatabaseUtil {

    public static Connection createConnection() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:~/grading-app");
        ds.setUser("");
        ds.setPassword("");
        Connection conn = ds.getConnection();

        return conn;
    }

    public static DSLContext createContext(Connection conn) {
        DSLContext create = DSL.using(conn, SQLDialect.H2);
        return create;
    }
}
