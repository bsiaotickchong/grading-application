
package courses;

import assignments.AssignmentMetaData;
import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.*;
import org.jooq.grading_app.db.h2.tables.records.CategoryRecord;
import org.jooq.grading_app.db.h2.tables.records.CourseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;

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

    public TimeOfYear getTimeOfYear() {
        return timeOfYear;
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

    public int getId() {
        return id;
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

    public AssignmentMetaData addAssignment(Category category, boolean extraCredit, String name) {
        try {
            AssignmentMetaData a = new AssignmentMetaData(this, category, extraCredit, name);
            return a;
        } catch (SQLException e) {
            LOG.error("Could not create AssignmentMetaData: {}", e.getMessage());
        }
        return null;
    }

    public List<AssignmentMetaData> getAssignmentMetaDatasForCategory(Category category) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            List<Assignment> assignments = create
                    .selectFrom(ASSIGNMENT)
                    .where(ASSIGNMENT.CATEGORY_ID.eq(category.getId()))
                    .fetchInto(Assignment.class);

            List<AssignmentMetaData> assignmentMetaDataList = new ArrayList<>();
            for (Assignment assignment : assignments) {
                assignmentMetaDataList.add(new AssignmentMetaData(this, assignment));
            }
            return assignmentMetaDataList;
        }
    }

    public List<AssignmentMetaData> getAllAssignmentMetaDatas() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            List<Assignment> assignments = new ArrayList<>();
            for (Category category : getCategories()) {
                List<Assignment> assignmentsForCategory = create
                        .selectFrom(ASSIGNMENT)
                        .where(ASSIGNMENT.CATEGORY_ID.eq(category.getId()))
                        .fetchInto(Assignment.class);
                assignments.addAll(assignmentsForCategory);
            }

            List<AssignmentMetaData> assignmentMetaDataList = new ArrayList<>();
            for (Assignment assignment : assignments) {
                assignmentMetaDataList.add(new AssignmentMetaData(this, assignment));
            }
            return assignmentMetaDataList;
        }
    }

    public Category addCategory(String categoryName) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            CategoryRecord categoryRecord = create.newRecord(CATEGORY);

            categoryRecord.setCourseId(this.id);
            categoryRecord.setName(categoryName);

            categoryRecord.store();

            return create
                    .selectFrom(CATEGORY)
                    .where(CATEGORY.ID.eq(categoryRecord.getId()))
                    .fetchOneInto(Category.class);
        } catch (SQLException e) {
            LOG.error("Could not create CategoryRecord");
            throw e;
        }
    }

    public List<Category> getCategories() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            return create
                    .selectFrom(CATEGORY)
                    .where(CATEGORY.COURSE_ID.eq(this.id))
                    .fetchInto(Category.class);
        }
    }

    public Object[] getCategoriesAsStrings() throws SQLException {
        return getCategories().stream()
                .map(Category::getName).toArray();
    }

    // get StudentTypes of the students enrolled in the course
    public List<StudentType> getEnrolledStudentTypes() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            return create
                    .select(STUDENT_TYPE.asterisk())
                    .from(ENROLLMENT
                            .join(STUDENT)
                            .on(ENROLLMENT.STUDENT_ID.eq(STUDENT.ID))
                            .join(STUDENT_TYPE)
                            .on(STUDENT_TYPE.ID.eq(STUDENT.STUDENT_TYPE_ID)))
                    .where(ENROLLMENT.COURSE_ID.eq(this.id))
                    .fetchInto(StudentType.class);
        }
    }

    public Object[] getStudentTypesAsStrings() throws SQLException {
        return getEnrolledStudentTypes().stream()
                .map(StudentType::getName).toArray();
    }

    public List<Student> getEnrolledStudents() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            return create
                    .select(STUDENT.asterisk())
                    .from(ENROLLMENT
                        .join(STUDENT)
                        .on(ENROLLMENT.STUDENT_ID.eq(STUDENT.ID)))
                    .where(ENROLLMENT.COURSE_ID.eq(this.id))
                    .fetchInto(Student.class);
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