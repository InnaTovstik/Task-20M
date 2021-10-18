package org.example;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    @Test
    public void shouldSelectAuthorId() {

        String exspected = "5df1e4a8-b729-415e-97aa-eaf9f3d77989";
        String firstName = "Taras";
        String lastName = "Shevchenko";
        assertEquals(exspected, BookService.selectAuthorId(firstName, lastName));
    }

    @Test
    public void shouldIsDuplicateBook() {
        String title = "Kateryna";
        assertTrue(BookService.isDuplicateBook(title));
    }

    @Test
    public void shouldIsNotDuplicateBook() {
        String title = "Ivan";
        assertFalse(BookService.isDuplicateBook(title));
    }
    @Test
    public void shouldselectBooksByAuthor() {
        Map<String, Integer> exspected = new HashMap<>();
        exspected.put("Kateryna", 49);
        exspected.put("Zibrane", 300);
        String firstName = "Taras";
        String lastName = "Shevchenko";
        assertEquals(exspected, BookService.getBooksByAuthor(firstName, lastName));
    }
}
