package com.example.ICEM_BACKEND.repository;

import com.example.ICEM_BACKEND.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
