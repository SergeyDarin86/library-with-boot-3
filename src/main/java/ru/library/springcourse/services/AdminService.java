package ru.library.springcourse.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdmin(){
        System.out.println("Только администратор может совершать действия на этой странице");
    }

}
