package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    static String[] authorInf = new String[2];// firstName, lastName
    static String[] bookInf = new String[4];// firstName, lastName, title, pagesCount
    static List<Book> listBooks ; //список книг заданного автора

    public static void main(String[] args) {
        System.out.println("Task 20");
        doIt();
    }

    static String[] inputAuthorInformation() {
        String[] name = new String[2];
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите firstName автора: ");
            name[0] = buffer.readLine();
            System.out.println("Введите lastName автора: ");
            name[1] = buffer.readLine();
        } catch (IOException ex) {
            System.out.println("Ошибка ввода данных об авторе");
        }
        return name;
    }

    // Ввод в консоль данных о книге
    static String[] inputBookInformation() {
        String[] book = new String[4];
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите firstName автора: ");
            book[0] = buffer.readLine();
            System.out.println("Введите lastName автора: ");
            book[1] = buffer.readLine();
            System.out.println("Введите название книги: ");
            book[2] = buffer.readLine();
            System.out.println("Введите количество страниц: ");
            book[3] = buffer.readLine();
        } catch (IOException ex) {
            System.out.println("Ошибка ввода данных о книге");
        }
        return book;
    }

    public static void doIt() {
        System.out.println("Какую операцию с БД хотите выполнить? Введите нужную цифру");
        System.out.println("1 - добавить книгу в БД.");
        System.out.println("2 - добавить автора в БД.");
        System.out.println("3 - вернуть список книг по имени и фамилии автора.");
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            String i = buffer.readLine();
            switch (i) {
                case "1":
                    bookInf = inputBookInformation();
                    String firstName = bookInf[0];
                    String lastName = bookInf[1];
                    String title = bookInf[2];
                    int pagesCount = Integer.parseInt(bookInf[3]);
                    BookService.addBook(firstName, lastName, title, pagesCount);
                    break;
                case "2":
                    authorInf = inputAuthorInformation();
                    firstName = authorInf[0];
                    lastName = authorInf[1];
                    BookService.addAuthor(firstName, lastName);
                    break;
                case "3":
                    authorInf = inputAuthorInformation();
                    firstName = authorInf[0];
                    lastName = authorInf[1];
                    listBooks = BookService.getBooksByAuthor(firstName, lastName);
                    System.out.println(listBooks);
                    break;
                default:
                    System.out.println("Не выбрана ни одна операция с БД");
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Ошибка ввода данных");
        }
    }
}