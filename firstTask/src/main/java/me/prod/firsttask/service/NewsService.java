package me.prod.firsttask.service;

import lombok.RequiredArgsConstructor;
import me.prod.firsttask.model.News;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NewsService {

    private final ConcurrentHashMap<Long, News> newsStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Optional<News> getById(Long id) {
        return Optional.ofNullable(newsStorage.get(id));
    }

    public List<News> getAll() {
        return new ArrayList<>(newsStorage.values());
    }

    public News create(News news) {
        Long id = idGenerator.getAndIncrement();
        news.setId(id);
        newsStorage.put(id, news);
        return news;
    }

    public Optional<News> update(News news) {
        if (news.getId() == null || !newsStorage.containsKey(news.getId())) {
            return Optional.empty();
        }
        newsStorage.put(news.getId(), news);
        return Optional.of(news);
    }

    public boolean deleteById(Long id) {
        return newsStorage.remove(id) != null;
    }
}