package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.model.User;
import com.example.eksamensprojekt.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController extends PMController {
    //private IUserRepository userRepository;

    public UserController(ApplicationContext context, @Value("user_DB") String impl) {
       //this.userRepository =(IUserRepository) context.getBean(impl);
    }


    @GetMapping("login")
    public String login() {
        return "index";
    }

    @PostMapping("login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = userRepository.getUser(userRepository.getUserid(username));
        if(user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            session.setAttribute("userid", user.getUserid());
            return "redirect:/userProjects/" + user.getUserid();
        }
        model.addAttribute("wrongCredentials", true);
        return "index";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        // invalidate session and return landing page
        session.invalidate();
        return "index";
    }

    @GetMapping("register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userRepository.registerUser(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameExists", true);
            return "register";
        }
    }

    @GetMapping("account/{userid}")
    public String getAccount(@PathVariable("userid") int userid, Model model, HttpSession session) {
        if (isLogged(session)) {
            User user = (User) session.getAttribute("user");
            if (user.getUserid() == userid) {
                model.addAttribute("user", user);
                return "account";
            } else {
                return "redirect:/userProjects";
            }
        } else {
            return "index";
        }
    }

    @GetMapping("account/delete/{userid}")
    public String deleteAccount(@PathVariable("userid") int userid, HttpSession session) {
        userRepository.deleteUser(userid);
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("account/update/{userid}")
    public String updateAccount(@PathVariable("userid") int userid, @ModelAttribute("user") User user, HttpSession session) {
        userRepository.updateUsername(userid, user.getUserName());
        userRepository.updateName(userid, user.getname());
        userRepository.updatePassword(userid, user.getPassword());

        session.setAttribute("user", user);

        return "redirect:/account/" + userid;
    }
}