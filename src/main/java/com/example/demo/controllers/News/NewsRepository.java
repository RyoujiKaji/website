package com.example.demo.controllers.News;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.News_folder.News;

public interface NewsRepository extends JpaRepository<News, Integer> { 

} 