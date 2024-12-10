package com.example.demo.controllers;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository; 

import com.example.demo.models.General;

//import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralRepository extends JpaRepository<General, Integer> { 

} 