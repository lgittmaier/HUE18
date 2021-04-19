package zimkand.de.todoapphue18;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Note {
    String title;
    String details;
    String dateTime;
    Calendar calendar;


    public Note() {
    }

    public Note(String title, String dateTime) {
        this.title = title;
        this.dateTime = dateTime;
    }

    public Note(String dateTime, String title, String details) {
        this.dateTime = dateTime;
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    @Override
    public String toString() {
        return dateTime + "  " + title + " ,  " + details;
    }
}
