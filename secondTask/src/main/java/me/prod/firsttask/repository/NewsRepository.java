package me.prod.firsttask.repository;

import me.prod.firsttask.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategoryId(Long categoryId);
}
