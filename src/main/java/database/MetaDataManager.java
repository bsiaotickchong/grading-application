package database;

import java.sql.SQLException;
import java.util.List;

public interface MetaDataManager {
    abstract List<MetaData> getAllMetaData() throws SQLException;
}
