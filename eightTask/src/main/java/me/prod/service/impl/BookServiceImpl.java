package me.prod.service.impl;

import me.prod.entity.Book;
import me.prod.entity.Category;
import me.prod.repository.BookRepository;
import me.prod.repository.CategoryRepository;
import me.prod.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Override
    public List<Book> findByCategoryName(String categoryName) {
        return bookRepository.findByCategoryName(categoryName);
    }

    @Override
    public Book createBook(Book book) {
        if (book.getCategory() != null && book.getCategory().getName() != null) {
            Optional<Category> existingCategory = categoryRepository.findByName(book.getCategory().getName());
            if (existingCategory.isPresent()) {
                book.setCategory(existingCategory.get());
            }
        }
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with id: " + id));

        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setDescription(book.getDescription());

        if (book.getCategory() != null && book.getCategory().getName() != null) {
            Optional<Category> existingCategory = categoryRepository.findByName(book.getCategory().getName());
            if (existingCategory.isPresent()) {
                bookToUpdate.setCategory(existingCategory.get());
            } else {
                bookToUpdate.setCategory(book.getCategory());
            }
        }

        return bookRepository.save(bookToUpdate);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }
}
