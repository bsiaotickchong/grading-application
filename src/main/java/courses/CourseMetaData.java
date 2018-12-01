package courses;

import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.Course;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;
import org.jooq.grading_app.db.h2.tables.records.CourseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.jooq.grading_app.db.h2.Tables.COURSE;
import static org.jooq.grading_app.db.h2.Tables.ENROLLMENT;
import static org.jooq.grading_app.db.h2.Tables.TIME_OF_YEAR;

public class CourseMetaData implements MetaData {

    private final static Logger LOG = LoggerFactory.getLogger(CourseMetaData.class);

    private int id;
    private String name;
    private TimeOfYear timeOfYear;
    private String description;

    public CourseMetaData(String name,
                          TimeOfYear timeOfYear,
                          String description) throws SQLException {
        this.name = name;
        this.timeOfYear = timeOfYear;
        this.description = description;

        createAndStoreRecord();
        LOG.debug("CourseMetaData added with ID: {}", this.id);
    }

    public CourseMetaData(Course course) throws SQLException {
        this.id = course.getId();
        this.name = course.getName();
        this.timeOfYear = getTimeOfYearFromId(course.getTimeOfYearId());
        this.description = course.getDescription();
    }

    // helper function TODO: place in manager
    private TimeOfYear getTimeOfYearFromId(int timeOfYearId) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(TIME_OF_YEAR)
                    .where(TIME_OF_YEAR.ID.eq(timeOfYearId))
                    .fetchOneInto(TimeOfYear.class);
        }
    }

    @Override
    public int createAndStoreRecord() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            CourseRecord courseRecord = create.newRecord(COURSE);

            courseRecord.setName(name);
            courseRecord.setTimeOfYearId(timeOfYear.getId());
            courseRecord.setDescription(description);

            int result = courseRecord.store();

            id = courseRecord.getId();
            return result;
        } catch (SQLException e) {
            LOG.error("Could not create CourseRecord");
            throw e;
        }
    }

    private CourseRecord getCourseRecord(Connection conn) {
        DSLContext create = H2DatabaseUtil.createContext(conn);
        return create.fetchOne(COURSE, COURSE.ID.eq(id));
    }

    public Course getCourse() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            return getCourseRecord(conn).into(Course.class);
        } catch (SQLException e) {
            LOG.error("Could not get Course");
            throw e;
        }
    }

    public int getEnrollmentCount() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            return create
                    .selectCount()
                    .from(ENROLLMENT)
                    .where(ENROLLMENT.COURSE_ID.eq(this.id))
                    .fetchOneInto(int.class);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void printMetaData() {
        try {
            String courseString = getCourse().toString();
            StringBuilder sb = new StringBuilder();

            sb.append(courseString);
            LOG.info(sb.toString());
        } catch (SQLException e) {
            LOG.error("Error printing CourseMetaData: " + e.getMessage());
        }
    }
}
