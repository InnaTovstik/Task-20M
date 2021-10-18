package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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

    //тест работает
    static Map<String, Integer> selectBooksByAuthor(String firstName, String lastName) {
        String sql = "SELECT title, pagesCount " +
                "FROM Books " +
                "LEFT JOIN Authors " +
                "ON Authors.id = Books.authorId " +
                "WHERE Authors.firstName = ? AND Authors.lastName = ? ";
        Map<String, Integer> mapBooks = new HashMap<>();
        try (PreparedStatement pstmt = connect().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String titleBook = rs.getString(1);
                int count = rs.getInt(2);
                mapBooks.put(titleBook, count);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения из БД");
        }
        return mapBooks;
    }

    //тест работает
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

    //тест работает
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