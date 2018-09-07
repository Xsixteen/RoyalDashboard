package com.ericulicny.homedashboard.rest;

import com.ericulicny.homedashboard.domain.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping("/api/login")
    public String login(LoginForm loginForm) {
        System.out.println("My login controller");
        System.out.println("User name is " + loginForm.getUsername());
        System.out.println("Passwird is " + loginForm.getPassword());
        Authentication request = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());

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