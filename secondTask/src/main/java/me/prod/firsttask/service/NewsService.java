package me.prod.firsttask.service;

import lombok.RequiredArgsConstructor;
import me.prod.firsttask.model.News;
import me.prod.firsttask.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public Optional<News> getById(Long id) {
        return newsRepository.findById(id);
    }

    public List<News> getAll() {
        return newsRepository.findAll();
    }

    public News create(News news) {
        return newsRepository.save(news);
    }

    public Optional<News> update(News news) {
        if (news.getId() == null || !newsRepository.existsById(news.getId())) {
            return Optional.empty();
        }
        return Optional.of(newsRepository.save(news));
    }

    public boolean deleteById(Long id) {
        if (!newsRepository.existsById(id)) {
            return false;
        }
        newsRepository.deleteById(id);
        return true;
    }

    public List<News> getNewsByCategoryId(Long categoryId) {
        return newsRepository.findByCategoryId(categoryId);
    }
}