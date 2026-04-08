package com.example.movieticketfirebase.model;

import java.util.ArrayList;
import java.util.List;

public class Showtime {
    private String id;
    private String movieId;
    private String theaterId;
    private String theaterName;
    private String room;
    private String startTimeText;
    private Long timestamp;
    private Long price;
    private List<String> bookedSeats = new ArrayList<>();

    public Showtime() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }

    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getStartTimeText() { return startTimeText; }
    public void setStartTimeText(String startTimeText) { this.startTimeText = startTimeText; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }

    public List<String> getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(List<String> bookedSeats) { this.bookedSeats = bookedSeats; }
}
