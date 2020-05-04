package com.example.cecilia.mybookshelf;

/**
 * Created by Cecilia on 01/09/2017.
 */

public class Book {
    protected String title;
    protected String original_publication_year;
    protected String pages;
    protected String ID;
    protected String author;
    protected String rating;
    protected String about;
    protected String imgUrl;
    protected String personalRating;
    protected String notes;
    protected String date_start;
    protected String date_finish;
    protected String priority;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_publication_year() {
        return original_publication_year;
    }

    public void setOriginal_publication_year(String original_publication_year) {
        this.original_publication_year = original_publication_year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(String personalRating) { this.personalRating = personalRating; }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_finish() {
        return date_finish;
    }

    public void setDate_finish(String date_finish) {
        this.date_finish = date_finish;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void printBook(){
        System.out.println(title + " - " + author + "(" + original_publication_year + ")"  + "\nRating: " + rating + "\nID: " + ID);

    }

    public String toString(){
        String s = title + " - " + author + "(" + original_publication_year + ")"  + "\nRating: " + rating + "\nID: " + ID;
        return s;
    }
}

