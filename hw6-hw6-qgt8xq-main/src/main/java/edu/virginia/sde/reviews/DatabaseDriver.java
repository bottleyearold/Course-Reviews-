package edu.virginia.sde.reviews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    private final String SQLfileName;
    private Connection connection;
    public final String USER_TABLE = "Users",
            USER_ID = "ID",
            USER_NAME = "Username",
            USER_PASS = "Password";

    public final String COURSE_TABLE = "Course",
            COURSE_ID = "ID",
            COURSE_SUBJ = "Subject",
            COURSE_NUM = "Number",
            COURSE_TITLE = "Title",
            COURSE_AVERAGE_RATING = "AverageRating";

    public final String REVIEW_TABLE = "Reviews",
            REVIEW_ID = "ID",
            REVIEW_COURSE = "CourseID",
            REVIEW_USERID = "UserID",
            REVIEW_RATINGS = "Rating",
            REVIEW_COMMENT = "Comment",
            REVIEW_TIMESTAMP = "Timestamp";

    public DatabaseDriver(Configuration configuration) {
        this.SQLfileName = configuration.getFileNameDatabase();
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The database is already connected");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + SQLfileName);
        connection.createStatement().execute("PRAGMA foreign_keys=ON");
        connection.setAutoCommit(false);

    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void checkConnection() throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("The database is already closed");
        }
    }

    public void createTable() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.execute(
                    """
                            CREATE TABLE IF NOT EXISTS Users ( 
                            ID INTEGER PRIMARY KEY,  
                            Username TEXT UNIQUE NOT NULL,  
                            Password TEXT NOT NULL);
                            """
            );

            statement.execute(
                    """
                            CREATE TABLE IF NOT EXISTS Courses( 
                            ID INTEGER PRIMARY KEY,  
                            Subject TEXT NOT NULL,  
                            Number INTEGER NOT NULL,  
                            Title TEXT NOT NULL,  
                            AvgerageRating REAL);
                            """
            );

            statement.execute(
                    """
                            CREATE TABLE IF NOT EXISTS Reviews( 
                            ID INTEGER PRIMARY KEY,  
                            CourseID INTEGER NOT NULL,  
                            UserID INTEGER NOT NULL,  
                            Rating INTEGER NOT NULL,  
                            Comment TEXT,  
                            TimeStamp TIMESTAMP NOT NULL,  
                            FOREIGN KEY(CourseID) REFERENCES Courses(ID) ON DELETE CASCADE,  
                            FOREIGN KEY(UserID) REFERENCES Users(ID) ON DELETE CASCADE);
                            """
            );
        }
        catch (SQLException e) {
            throw new SQLException("Could not create table", e);
        }

    }

    public void addUser(User user) throws SQLException {
        checkConnection();
        var query = "INSERT INTO Users(Username, Password) VALUES (?, ?);";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void addCourse(Course course) throws SQLException {
        checkConnection();
        var query = "INSERT INTO Courses(Subject, Number, Title, AverageRating) VALUES (?, ?, ?, ?);";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, course.getSubject());
            preparedStatement.setInt(2, course.getNumber());
            preparedStatement.setString(3, course.getTitle());
            preparedStatement.setDouble(4, course.getAverageRating());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }

    public void addReview(Review review) throws SQLException {
        //System.out.println("IM HERE 1");
        checkConnection();
        //System.out.println("IM HERE 2");
        var query = "INSERT INTO Reviews(CourseID, UserID, Rating, Comment, TimeStamp) VALUES (?, ?, ?, ?, ?);";
        try (var preparedStatement = connection.prepareStatement(query)) {
            //System.out.println("IM HERE 3");
            preparedStatement.setInt(1, review.getCourseID());
            preparedStatement.setInt(2, review.getUserID());
            preparedStatement.setInt(3, review.getRating());
            preparedStatement.setString(4, review.getComment());
            preparedStatement.setTimestamp(5, review.getTimestamp());
            preparedStatement.executeUpdate();
            //System.out.println("IM HERE 4");
            updateAverageRating(review.getCourseID());
           // System.out.println("IM HERE 5");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("IM HERE 6");
            rollback();
            throw e;
        }
    }

    public void deleteReview(Review review) throws SQLException {
        checkConnection();
        var query = "DELETE FROM Reviews WHERE UserID = ? AND CourseID = ?;";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, review.getUserID());
            preparedStatement.setInt(2, review.getCourseID());
            preparedStatement.executeUpdate();
            updateAverageRating(review.getCourseID());
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }
    public List<Course> getAllCourses () throws SQLException {
            checkConnection();
            var courses = new ArrayList<Course>();
            var query = "SELECT * FROM Courses";
            try (var preparedStatement = connection.prepareStatement(query);
                 var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(buildCourse(resultSet));
                }
            }
            return courses;
    }

        public List<Review> getAllReviewsByUserID ( int userID) throws SQLException {
            checkConnection();
            var reviews = new ArrayList<Review>();
            var query = "SELECT * FROM Reviews WHERE UserID = ?";
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        reviews.add(buildReview(resultSet));
                    }
                }
            }
            return reviews;
        }

        public List<Review> getAllReviewsByCourseID ( int courseID) throws SQLException {
            checkConnection();
            var reviews = new ArrayList<Review>();
            var query = "SELECT * FROM Reviews WHERE CourseID = ?;";
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, courseID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        reviews.add(buildReview(resultSet));
                    }
                }
            }
            return reviews;
        }

    private Course buildCourse (ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt("ID");
        var subject = resultSet.getString("Subject");
        var number = resultSet.getInt("Number");
        var title = resultSet.getString("Title");
        var avgRating = resultSet.getDouble("AverageRating");
        return new Course(id, subject, number, title, avgRating);
    }
        private User buildUser (ResultSet resultSet) throws SQLException {
            var id = resultSet.getInt("ID");
            var username = resultSet.getString("Username");
            var password = resultSet.getString("Password");
            return new User(id, username, password);
        }


        private Review buildReview (ResultSet resultSet) throws SQLException {
            var id = resultSet.getInt("ID");
            var courseID = resultSet.getInt("CourseID");
            var userID = resultSet.getInt("UserID");
            var rating = resultSet.getInt("Rating");
            var comment = resultSet.getString("Comment");
            var timeStamp = resultSet.getTimestamp("TimeStamp");
            return new Review(id, courseID, userID, rating, comment, timeStamp);
        }

    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE Username = ?;";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildUser(resultSet);
                }
            }
        }
        return null;
    }

    public Course getCourse(int id) throws SQLException {
        String query = "SELECT * FROM Courses WHERE ID = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildCourse(resultSet);
                }
            }
        }
        return null;
    }
    public Review getReviewByForeignKeys(int userID, int courseID) throws SQLException {
        checkConnection();
        var query = "SELECT * FROM Reviews WHERE UserID = ? AND CourseID = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, courseID);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildReview(resultSet);
                }
            }
        }
        return null;
    }

    public void updateAverageRating ( int courseID) throws SQLException {
//        System.out.println("IM HERE 1");
            checkConnection();
//        System.out.println("IM HERE 2");
            var avgRatingQuery = "SELECT AVG(Rating) AS AverageRating FROM Reviews WHERE CourseID = ?;";
            var updateCoursesQuery = "UPDATE Courses SET AverageRating = ? WHERE ID = ?";
            try (var avgRatingStatement = connection.prepareStatement(avgRatingQuery)) {
//                System.out.println("IM HERE 3");
                avgRatingStatement.setInt(1, courseID);
                try (var resultSet = avgRatingStatement.executeQuery()) {
//                    System.out.println("IM HERE 4");
                    if (resultSet.next()) {
//                        System.out.println("IM HERE 4.5");
                        var averageRating = resultSet.getDouble("AverageRating");
                        System.out.println(averageRating);
                        if (getAllReviewsByCourseID(courseID).isEmpty()) {
                            averageRating = 0;
                           // System.out.println("IM HERE 4.6");
                        }
                        try (var updateStatement = connection.prepareStatement(updateCoursesQuery)) {
                            //System.out.println("IM HERE 5");
                            updateStatement.setDouble(1, averageRating);
                            updateStatement.setInt(2, courseID);
                            updateStatement.executeUpdate();
//                            System.out.println("IM HERE 6");
                        }
                    }
                }
            }
        }
    public boolean isCourseInDatabase (Course course) throws SQLException {
        checkConnection();
        String query = "SELECT 1 FROM Courses WHERE Subject = ? AND Number = ? AND Title = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, course.getSubject());
            preparedStatement.setInt(2, course.getNumber());
            preparedStatement.setString(3, course.getTitle().strip());  // Using .strip() to remove any trailing spaces
            try (var resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    return resultSet.next();
                }
            }
        }
        return false;  // Return false if the course does not exist
    }

        public boolean isUserInDatabase (String username) throws SQLException {
            checkConnection();
            String query = "SELECT EXISTS(SELECT 1 FROM Users WHERE Username = ?)";
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);  // Get the boolean value from the first column
                    }
                }
            }
            return false;  // Return false if the user does not exist
        }


    }

