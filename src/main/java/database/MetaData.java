package database;

import java.sql.SQLException;

public interface MetaData {
    int createAndStoreRecord() throws SQLException;
    void printMetaData();
}
