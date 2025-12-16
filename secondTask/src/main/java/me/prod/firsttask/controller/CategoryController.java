package me.prod.firsttask.controller;

import lombok.RequiredArgsConstructor;
import me.prod.firsttask.model.Category;
import me.prod.firsttask.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return categoryService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория с id " + id + " не найдена."));
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping
    public Category update(@RequestBody Category category) {
        return categoryService.update(category)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория с id " + category.getId() + " не найдена."));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        if (!categoryService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория с id " + id + " не найдена.");
        }
    }
}
