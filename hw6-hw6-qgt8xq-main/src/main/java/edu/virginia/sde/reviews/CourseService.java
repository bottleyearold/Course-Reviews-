package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.util.List;


public class CourseService {
    private final DatabaseDriver databaseDriver;

    public CourseService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public List<Course> getCourses() {
        try{
            databaseDriver.connect();
            databaseDriver.createTable();
            var courses = databaseDriver.getAllCourses();
            databaseDriver.disconnect();
            return courses;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void addCourse(Course course) {
        try {
            databaseDriver.connect();
            if (databaseDriver.isCourseInDatabase(course)) {
                databaseDriver.disconnect();
                throw new CourseExistAlreadyException();
            }
            databaseDriver.addCourse(course);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
