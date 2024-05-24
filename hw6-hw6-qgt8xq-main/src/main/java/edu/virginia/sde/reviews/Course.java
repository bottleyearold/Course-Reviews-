package edu.virginia.sde.reviews;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class Course {
    private final int id;
    private final String subject;
    private final int number;
    private final String title;
    private final double averageRating;

    public Course(String subject, int number, String title) {
        this.id = -1;
        this.subject = subject;
        this.number = number;
        this.title = title;
        this.averageRating = -1;
    }

    public Course(int id, String subject, int number, String title, double averageRating) {
        this.id = id;
        this.subject = subject;
        this.number = number;
        this.title = title;
        this.averageRating = averageRating;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public double getAverageRating() {
        return averageRating;
    }


    public String numToString(){
        var numberString = String.valueOf(number);
        return "0".repeat(4-numberString.length()) + numberString;
    }

    public String toString(){
        var stringBuilder = new StringBuilder();
        stringBuilder.append(subject)
                .append(" ")
                .append(numToString())
                .append(": ")
                .append(title)
                .append("\n Rating: ");
        if(averageRating != -1){
            var truncatedRating = BigDecimal.valueOf(getAverageRating()).setScale(2, RoundingMode.DOWN);
            stringBuilder.append(truncatedRating).append("/5.00");
        }else{
            stringBuilder.append("N/A");
        }
        return stringBuilder.toString();
    }

}
