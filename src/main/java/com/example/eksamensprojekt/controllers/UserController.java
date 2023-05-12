package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private IUserRepository userRepository;

//    public UserController(ApplicationContext context, @Value("eksamensprojekt_DB") String impl) {
//        this.userRepository =(IUserRepository) context.getBean(impl);
//    }
}
