package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    static void addBook(String firstName, String lastName, String title, int pagesCount) {
        if (!isDuplicateBook(title)) {
            String authorId = selectAuthorId(firstName, lastName);
            Book book = new Book(title, authorId, pagesCount);
            System.out.println(book);
            if (!isDuplicateBook(title)) {
                String sql = "INSERT " +
                        "INTO Books(id, title, authorId, pagesCount)" +
                        " VALUES(?, ?, ?, ?)";
                try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
                    pstmt.setString(1, book.getId());
                    pstmt.setString(2, book.getTitle());
                    pstmt.setString(3, book.getAuthorId());
                    pstmt.setInt(4, book.getPagesCount());
                    pstmt.executeUpdate();
                    System.out.println("В БД внесена информация об : " + book);
                } catch (SQLException e) {
                    System.out.println("Ошибка записи книги в базу данных");
                }
            } else {
                System.out.println("Такая книга есть в БД");
            }
        }
    }

    static void addAuthor(String firstName, String lastName) {
        String authorId = selectAuthorId(firstName, lastName);
        if (authorId == null) {
            System.out.println("Такого автора еще нет в БД. Заносим его данные.");
            Author author = new Author(firstName, lastName);
            String sql = "INSERT " +
                    "INTO Authors(id, firstName, lastName)" +
                    " VALUES(?, ?, ?)";
            try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
                pstmt.setString(1, author.getId());
                pstmt.setString(2, author.getFirstName());
                pstmt.setString(3, author.getLastName());
                pstmt.executeUpdate();
                System.out.println("В БД внесена информация об : " + author);
            } catch (SQLException e) {
                System.out.println("Ошибка записи автора в базу данных");
            }
        }
    }

    // Method dosen't read from DB (with "SELECT id")
    static List<Book> getBooksByAuthor(String firstName, String lastName) {
        String sql = "SELECT id, title, authorId, pagesCount " +
                "FROM Books " +
                "LEFT JOIN Authors " +
                "ON Authors.id = Books.authorId " +
                "WHERE Authors.firstName = ? AND Authors.lastName = ? ";
        List<Book> listBooks = new ArrayList<>();
        try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String titleBook = rs.getString(2);
                String authorId = rs.getString(3);
                int count = rs.getInt(4);
                listBooks.add(new Book(id, titleBook, authorId, count));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения из БД");
        }
        return listBooks;
    }

    // Method read from DB (without "SELECT id")
    static List<Book> getBooksByAuthor1(String firstName, String lastName) {
        String sql = "SELECT title, authorId, pagesCount " +
                "FROM Books " +
                "LEFT JOIN Authors " +
                "ON Authors.id = Books.authorId " +
                "WHERE Authors.firstName = ? AND Authors.lastName = ? ";
        List<Book> listBooks = new ArrayList<>();
        try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String titleBook = rs.getString(1);
                String authorId = rs.getString(2);
                int count = rs.getInt(3);
                listBooks.add(new Book("1", titleBook, authorId, count));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения из БД");
        }
        return listBooks;
    }


// Check to duplicate book
   static boolean isDuplicateBook(String title) {
        String sql = "SELECT title " +
                "FROM Books " +
                "LEFT JOIN Authors " +
                "ON Authors.id = Books.authorId " +
                "WHERE Books.title = ? AND Books.authorId = Authors.id ";
        String titleBook = null;
        boolean flag = true;
        try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                titleBook = rs.getString(1);
                System.out.println("titleBook= " + titleBook);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения из БД");
        }
        if (titleBook == null) {
            flag = false;
        }
        return flag;
    }

    //
    static String selectAuthorId(String firstName, String lastName) {
        String sql = "SELECT id "
                + "FROM Authors " +
                "WHERE firstName = ? AND lastName = ?";
        String id = null;
        try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения из БД");
        }
        return id;
    }

    /*Connect to the test.db database.
         return the Connection object
   */
    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/MyData/lib1.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.out.println("Ошибка соединения с файлом базы данных");
        }
        return conn;
    }
}