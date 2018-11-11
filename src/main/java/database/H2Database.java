package database;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class H2Database {

    Connection conn;

    public H2Database() throws SQLException {
        conn = createConnection();
    }

    private Connection createConnection() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:~/grading-app");
        ds.setUser("");
        ds.setPassword("");
        Connection conn = ds.getConnection();

        return conn;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    public Connection getConnection() {
        return conn;
    }
}
