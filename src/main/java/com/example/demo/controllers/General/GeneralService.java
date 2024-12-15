package com.example.demo.controllers.General;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.General.General;
import com.example.demo.models.Users.User;

import java.util.Optional;

@Service
public class GeneralService {

    @Autowired
    private GeneralRepository generalRepository;

 /*    public List<General> getAllUsers() {
        return userRepository.findAll();
    } */

    public Optional<General> getVisitsById(int id) {
        return generalRepository.findById(id);
    }

    public General updateVisits(General general) {
        return generalRepository.save(general);
    }

   /*  public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    } */
}