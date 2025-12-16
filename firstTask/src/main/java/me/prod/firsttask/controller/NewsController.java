package me.prod.firsttask.controller;

import lombok.RequiredArgsConstructor;
import me.prod.firsttask.model.News;
import me.prod.firsttask.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    public News getById(@PathVariable Long id) {
        return newsService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Новость с ID " + id + " не найдена."));
    }

    @GetMapping
    public List<News> getAll() {
        return newsService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public News create(@RequestBody News news) {
        return newsService.create(news);
    }

    @PutMapping
    public News update(@RequestBody News news) {
        return newsService.update(news)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Новость с ID " + news.getId() + " не найдена."));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!newsService.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Новость с ID " + id + " не найдена.");
        }
    }
}