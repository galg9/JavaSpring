package me.prod.controller;

import me.prod.entity.Book;
import me.prod.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public Book findByTitleAndAuthor(
            @RequestParam String title,
            @RequestParam String author) {
        return bookService.findByTitleAndAuthor(title, author)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found"));
    }

    @GetMapping("/category/{categoryName}")
    public List<Book> findByCategoryName(@PathVariable String categoryName) {
        return bookService.findByCategoryName(categoryName);
    }

    @GetMapping
    public List<Book> findAllBooks() {
        return bookService.findAllBooks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
