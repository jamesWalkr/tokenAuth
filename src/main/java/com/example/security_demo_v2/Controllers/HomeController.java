package com.example.security_demo_v2.Controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {


    @GetMapping
    public String home() {
        return "Hello, Spring Security";
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String user(){
        return " Hello User";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin(){
        return "Hello Admin";
    }


}
