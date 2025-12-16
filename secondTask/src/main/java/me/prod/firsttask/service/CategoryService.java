package me.prod.firsttask.service;

import lombok.RequiredArgsConstructor;
import me.prod.firsttask.model.Category;
import me.prod.firsttask.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> update(Category category) {
        if (category.getId() == null || !categoryRepository.existsById(category.getId())) {
            return Optional.empty();
        }
        return Optional.of(categoryRepository.save(category));
    }

    public boolean deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
