package com.eulicny.homedashboard.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/api/login")
    public String login(@RequestParam(value="username") String name, @RequestParam(value="password") String password) {
        System.out.println("My login controller");
        System.out.println("User name is " + name);
        System.out.println("Passwird is " + password);
        Authentication request = new UsernamePasswordAuthenticationToken(name, password);

        Authentication result = authenticationManager.authenticate(request);
        System.out.println(result.getPrincipal());
        System.out.println(result.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(result);
        return "ok";
    }

}