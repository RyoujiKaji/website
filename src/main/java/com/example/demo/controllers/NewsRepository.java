package com.example.demo.controllers;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.News;

public interface NewsRepository extends JpaRepository<News, Integer> { 

} 