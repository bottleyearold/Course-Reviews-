package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {

    private int id;
    private final int courseID;
    private final int userID;
    private final int rating;
    private final String comment;
    private final Timestamp timestamp;

    public Review(int courseID, int userID, int rating, String comment, Timestamp timestamp) {
        this.courseID = courseID;
        this.userID = userID;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Review(int id, int courseID, int userID, int rating, String comment, Timestamp timestamp) {
        this.id = id;
        this.courseID = courseID;
        this.userID = userID;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public int getCourseID() {
        return courseID;
    }
    public int getUserID() {
        return userID;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        if (comment.isEmpty()){
            return "N/A";
        }
        return comment;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

}
