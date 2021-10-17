package org.example;

import java.util.UUID;

public class Book {
    String id;
    String title;
    String authorId;
    int pagesCount;

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authorId='" + authorId + '\'' +
                ", pagesCount=" + pagesCount +
                '}';
    }

    public Book(String title, String authorId, int pagesCount) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.authorId = authorId;
        this.pagesCount = pagesCount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getPagesCount() {
        return pagesCount;
    }
}
