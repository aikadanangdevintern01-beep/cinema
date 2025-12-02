package hunglcb.example.module5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(){
        return "home/home";
    }
    @GetMapping("/detail")
    public String detail(){
        return "home/detail";
    }
    @GetMapping("/booking")
    public String book(){
        return "home/chair";
    }
    @GetMapping("/login")
    public String login(){
        return "home/login";
    }
}
