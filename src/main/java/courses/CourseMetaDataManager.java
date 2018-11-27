package courses;

import database.H2DatabaseUtil;
import database.MetaData;
import database.MetaDataManager;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.grading_app.db.h2.tables.Course.COURSE;

public class CourseMetaDataManager implements MetaDataManager {

    private final static Logger LOG = LoggerFactory.getLogger(CourseMetaDataManager.class);

    public CourseMetaDataManager() {

    }

    @Override
    public List<MetaData> getAllMetaData() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            List<Course> courses = create
                    .selectFrom(COURSE)
                    .fetchInto(Course.class);

            List<MetaData> metaDatas = new ArrayList<>();
            for (Course course : courses) {
                metaDatas.add(new CourseMetaData(course));
            }

            return metaDatas;
        }
    }
}
