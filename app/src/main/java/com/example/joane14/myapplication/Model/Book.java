package com.example.joane14.myapplication.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joane14 on 04/08/2017.
 */

public class Book implements Serializable{


    public Book(){

    }

    String bookId, bookTitle, description, publishedDate, bookFilename;
    Float bookOriginalPrice;


    List<Author> authors;

    List<GenreModel> genres;

    public void setBookFilename(String bookFilename){
        this.bookFilename = bookFilename;
    }

    public String getBookFilename(){
        return this.bookFilename;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookDescription(String bookDescription) {
        this.description = bookDescription;
    }

    public void setBookPublishedDate(String bookPublishedDate) {
        this.publishedDate = bookPublishedDate;
    }

    public void setBookOriginalPrice(Float bookOriginalPrice) {
        this.bookOriginalPrice = bookOriginalPrice;
    }

    public void setBookAuthor(List<Author> bookAuthor) {
        this.authors = bookAuthor;
    }

    public void setBookGenre(List<GenreModel> bookGenre) {
        this.genres = bookGenre;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookDescription() {
        return description;
    }

    public String getBookPublishedDate() {
        return publishedDate;
    }

    public Float getBookOriginalPrice() {
        return bookOriginalPrice;
    }

    public List<Author> getBookAuthor() {
        return authors;
    }

    public List<GenreModel> getBookGenre() {
        return genres;
    }
}
