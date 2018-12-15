package assignments;

import courses.CourseMetaData;
import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.*;
import org.jooq.grading_app.db.h2.tables.records.AssignmentRecord;
import org.jooq.grading_app.db.h2.tables.records.AssignmentWeightExceptionRecord;
import org.jooq.grading_app.db.h2.tables.records.AssignmentWeightRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;
import static org.jooq.impl.DSL.selectFrom;

public class AssignmentMetaData implements MetaData {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentMetaData.class);

    private int id;
    private Category category;
    private Boolean extraCredit;
    private String name;
    private CourseMetaData courseMetaData;

    public AssignmentMetaData(CourseMetaData courseMetaData,
                              Category category,
                              Boolean extraCredit,
                              String name) throws SQLException {
        this.category = category;
        this.extraCredit = extraCredit;
        this.name = name;

        this.courseMetaData = courseMetaData;

        createAndStoreRecord();
        LOG.debug("StudentMetaData added with ID: {}", this.id);
    }

    public AssignmentMetaData(CourseMetaData courseMetaData,
                              Assignment assignment) throws SQLException {
        this.id = assignment.getId();
        this.category = getCategoryFromId(assignment.getCategoryId());
        this.extraCredit = assignment.getExtracredit();
        this.name = assignment.getName();

        this.courseMetaData = courseMetaData;
    }

    @Override
    public int createAndStoreRecord() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            AssignmentRecord assignmentRecord = create.newRecord(ASSIGNMENT);

            assignmentRecord.setCategoryId(category.getId());
            assignmentRecord.setExtracredit(extraCredit);
            assignmentRecord.setName(name);

            int result = assignmentRecord.store();
            this.id = assignmentRecord.getId();

            // for each StudentType, create an AssignmentWeightRecord
            for (StudentType studentType : courseMetaData.getEnrolledStudentTypes()) {
                AssignmentWeightRecord assignmentWeightRecord = create.newRecord(ASSIGNMENT_WEIGHT);
                assignmentWeightRecord.setAssignmentId(this.id);
                assignmentWeightRecord.setStudentTypeId(studentType.getId());

                result = assignmentWeightRecord.store();
            }

            return result;
        } catch (SQLException e) {
            LOG.error("Could not create AssignmentRecord");
            throw e;
        }
    }

    public int setAssignmentName(String name) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setName(name);
            int res = assignmentRecord.store();
            this.name = name;
            return res;
        } catch (SQLException e){
            LOG.error("Could not set name");
            throw e;
        }
    }

    public int setCategory(Category category) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setCategoryId(category.getId());
            int res = assignmentRecord.store();
            this.category = category;
            return res;
        } catch(SQLException e){
            LOG.error("Could not set category");
            throw e;
        }
    }

    //right now, I just leave extra point as it was, and we can change this after ask
    //professor about the implementation she wants.

    private int setExtraCredit(Boolean extra_credit) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setExtracredit(extra_credit);
            int res = assignmentRecord.store();
            this.extraCredit = extra_credit;
            return res;
        } catch(SQLException e){
            LOG.error("Could not set category");
            throw e;
        }
    }
    //get all the students' grade who takes the assignments

    private List<StudentGrade> getStudentsGrade() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .select(STUDENT_GRADE.asterisk())
                    .from(STUDENT_GRADE
                            .join(ASSIGNMENT)
                            .on(STUDENT_GRADE.ASSIGNMENT_ID.eq(ASSIGNMENT.ID)))
                    .where(ASSIGNMENT.ID.eq(this.id))
                    .fetchInto(StudentGrade.class);
        }
    }
    //get all the students who have taken the assignment

    public List<Student> getStudent() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(STUDENT)
                    .join(STUDENT_GRADE).on(STUDENT.ID.eq(STUDENT_GRADE.STUDENT_ID))
                    .where(STUDENT_GRADE.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(Student.class);
        }
    }
    //get information inside assignment weight

    public List<AssignmentWeight> getWeights() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(AssignmentWeight.class);
        }
    }

    public AssignmentWeight getWeightForStudentType(StudentType studentType) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .and(ASSIGNMENT_WEIGHT.STUDENT_TYPE_ID.eq(studentType.getId()))
                    .fetchOneInto(AssignmentWeight.class);
        }
    }

    public int setWeightForStudentType(StudentType studentType, double weight) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.update(ASSIGNMENT_WEIGHT)
                    .set(ASSIGNMENT_WEIGHT.WEIGHT_PERCENT, weight)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .and(ASSIGNMENT_WEIGHT.STUDENT_TYPE_ID.eq(studentType.getId()))
                    .execute();
        }
    }

    public boolean hasWeightExceptionForStudent(StudentMetaData studentMetaData) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .fetchExists(
                            selectFrom(ASSIGNMENT_WEIGHT_EXCEPTION)
                                    .where(ASSIGNMENT_WEIGHT_EXCEPTION.STUDENT_ID.eq(studentMetaData.getId()))
                                    .and(ASSIGNMENT_WEIGHT_EXCEPTION.ASSIGNMENT_ID.eq(this.id))
                    );
        }
    }

    // return either the assignment's weight or the weight exception for the student if it exists
    public Double getWeightForStudent(StudentMetaData studentMetaData) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            if (hasWeightExceptionForStudent(studentMetaData)) {
                return create
                        .selectFrom(ASSIGNMENT_WEIGHT_EXCEPTION)
                        .where(ASSIGNMENT_WEIGHT_EXCEPTION.STUDENT_ID.eq(studentMetaData.getId()))
                        .and(ASSIGNMENT_WEIGHT_EXCEPTION.ASSIGNMENT_ID.eq(this.id))
                        .fetchOneInto(AssignmentWeightException.class)
                        .getWeightPercent();
            }

            // else return assignment weight for the student's type
            return create
                    .selectFrom(ASSIGNMENT_WEIGHT)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .and(ASSIGNMENT_WEIGHT.STUDENT_TYPE_ID.eq(studentMetaData.getStudentType().getId()))
                    .fetchOneInto(AssignmentWeight.class)
                    .getWeightPercent();
        }
    }

    public int setWeightExceptionForStudent(StudentMetaData studentMetaData, double weight) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            if (hasWeightExceptionForStudent(studentMetaData)) {
                return create
                        .update(ASSIGNMENT_WEIGHT_EXCEPTION)
                        .set(ASSIGNMENT_WEIGHT_EXCEPTION.WEIGHT_PERCENT, weight)
                        .execute();
            }

            // else, create a new weight exception row
            AssignmentWeightExceptionRecord assignmentWeightExceptionRecord = create.newRecord(ASSIGNMENT_WEIGHT_EXCEPTION);
            assignmentWeightExceptionRecord.setStudentId(studentMetaData.getId());
            assignmentWeightExceptionRecord.setAssignmentId(this.id);
            assignmentWeightExceptionRecord.setWeightPercent(weight);

            return assignmentWeightExceptionRecord.store();
        }
    }

    //get all the exception weight

    public List<AssignmentWeightException> getException() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT_EXCEPTION)
                    .where(ASSIGNMENT_WEIGHT_EXCEPTION.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(AssignmentWeightException.class);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getExtraCredit() {
        return extraCredit;
    }

    private AssignmentRecord getAssignmentRecord(Connection conn){
        DSLContext create = H2DatabaseUtil.createContext(conn);
        return create.fetchOne(ASSIGNMENT, ASSIGNMENT.ID.eq(id));
    }

    private Category getCategoryFromId(int categoryId) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(CATEGORY)
                    .where(CATEGORY.ID.eq(categoryId))
                    .fetchOneInto(Category.class);
        }
    }

    @Override
    public void printMetaData(){
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            String assignmentString = getAssignmentRecord(conn).into(Assignment.class).toString();
            StringBuilder sb = new StringBuilder();

            sb.append(name);
            sb.append(",\t");
            sb.append(assignmentString);
            sb.append(",\t");
            sb.append(extraCredit.toString());
            sb.append(",\t");
            sb.append(category.toString());

            for(Student student : getStudent()){
                sb.append(student.toString());
                sb.append("\t");
            }

            LOG.info(sb.toString());
        } catch (SQLException e) {
            LOG.error("Error printing AssignmentMetaData: " + e.getMessage());
        }
    }

}
