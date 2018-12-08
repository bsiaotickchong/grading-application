package assignments;

import courses.CourseMetaData;
import database.H2DatabaseUtil;
import database.MetaData;
import database.MetaDataManager;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.grading_app.db.h2.tables.Assignment.ASSIGNMENT;

public class AssignmentsMetaDataManager implements MetaDataManager {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentsMetaDataManager.class);

    @Override
    public List<MetaData> getAllMetaData() throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

            List<Assignment> assignments = create
                    .selectFrom(ASSIGNMENT)
                    .fetchInto(Assignment.class);

            List<MetaData> metaDatas = new ArrayList<>();
            for (Assignment assign : assignments){
                metaDatas.add(new AssignmentsMetaData(assign));
            }

            return metaDatas;
        }
    }

}
