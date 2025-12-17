package me.prod.service;

import me.prod.entity.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Optional;

public interface BookService {
    
    @Cacheable(value = "bookByTitleAndAuthor", key = "#title + '-' + #author")
    Optional<Book> findByTitleAndAuthor(String title, String author);
    
    @Cacheable(value = "booksByCategory", key = "#categoryName")
    List<Book> findByCategoryName(String categoryName);
    
    @Caching(evict = {
        @CacheEvict(value = "bookByTitleAndAuthor", key = "#book.title + '-' + #book.author"),
        @CacheEvict(value = "booksByCategory", key = "#book.category.name")
    })
    Book createBook(Book book);
    
    @Caching(evict = {
        @CacheEvict(value = "bookByTitleAndAuthor", allEntries = true),
        @CacheEvict(value = "booksByCategory", allEntries = true)
    })
    Book updateBook(Long id, Book book);
    
    @Caching(evict = {
        @CacheEvict(value = "bookByTitleAndAuthor", allEntries = true),
        @CacheEvict(value = "booksByCategory", allEntries = true)
    })
    void deleteBook(Long id);
    
    List<Book> findAllBooks();
}
