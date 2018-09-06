package com.ericulicny.homedashboard.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping("/api/login")
    public String login(@RequestParam(value="username") String name, @RequestParam(value="password") String password) {
        System.out.println("My login controller");
        System.out.println("User name is " + name);
        System.out.println("Passwird is " + password);
        Authentication request = new UsernamePasswordAuthenticationToken(name, password);

        Authentication result = authenticationManager.authenticate(request);
        log.info(result.getPrincipal().toString());
        log.info(result.getAuthorities().toString());
        SecurityContextHolder.getContext().setAuthentication(result);

        if(result.isAuthenticated()) {
            return "redirect:/index.html";
        } else {
            return "FAIL";
        }
    }

}