package edu.virginia.sde.reviews;
import java.sql.SQLException; // For handling SQL exceptions
import java.util.List; // To use List collection

public class ReviewService {
    private DatabaseDriver databaseDriver;

    public ReviewService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void addReview(Review review) {
        try {

            databaseDriver.connect();
            // System.out.println("IM HERE 2");
            databaseDriver.addReview(review);
           // System.out.println("IM HERE 3");
            databaseDriver.commit();
          //  System.out.println("IM HERE 4");
            databaseDriver.disconnect();
         //   System.out.println("IM HERE 5");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReview(Review review) {
        try {
            databaseDriver.connect();
            databaseDriver.deleteReview(review);
            databaseDriver.commit();
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Review> getReviewsByUserId(int userId) {
        try {
            databaseDriver.connect();
            List<Review> reviews = databaseDriver.getAllReviewsByUserID(userId);
            databaseDriver.disconnect();
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Review> getReviewsByCourseId(int courseId) {
        try {
            databaseDriver.connect();
            List<Review> reviews = databaseDriver.getAllReviewsByCourseID(courseId);
            databaseDriver.disconnect();
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Course getCourseById(int id) {
        try {
            databaseDriver.connect();
            Course course = databaseDriver.getCourse(id);
            databaseDriver.disconnect();
            return course;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Review getReview(int userId, int courseId) {
        try {
            databaseDriver.connect();
            Review review = databaseDriver.getReviewByForeignKeys(userId, courseId);
            databaseDriver.disconnect();
            return review;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getReviewString(Review review) {
        var stringBuilder = new StringBuilder();
        var course = getCourseById(review.getCourseID());
        stringBuilder.append(course.getSubject())
                .append(" - ")
                .append(course.getNumber())
                .append("\nRating: ")
                .append(review.getRating())
                .append("/5\nComment: ")
                .append(review.getComment())
                .append("\nTime Stamp: ")
                .append(review.getTimestamp());
        return stringBuilder.toString();
    }

    public String getReviewStringNoCourseName(Review review) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Rating: ")
                .append(review.getRating())
                .append("/5\nComment: ")
                .append(review.getComment())
                .append("\nTime Stamp: ")
                .append(review.getTimestamp());
        return stringBuilder.toString();
    }
}


