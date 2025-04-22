package com.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "film")
public class Film {

    private int id;
    private String title;
    private int year;
    private Date expireDate;
    // todo: should this be a list/set?
    private StreamingService streamingService;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILM_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "TITLE", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "YEAR")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Column(name = "STREAMING_SERVICE")
    public StreamingService getStreamingService() {
        return streamingService;
    }

    public void setStreamingService(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public String toString() {
        return title;
    }
}
