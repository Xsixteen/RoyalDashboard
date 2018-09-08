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
        Authentication request = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());

        Authentication result = authenticationManager.authenticate(request);
        log.info(result.getPrincipal().toString());
        log.info(result.getAuthorities().toString());
        SecurityContextHolder.getContext().setAuthentication(result);

        if(result.isAuthenticated()) {
            log.info("Login Successful");
            return "redirect:/index.html";
        } else {
            log.info("Login Failed!  Attempted username=" + loginForm.getUsername());
            return "redirect:/login.html?failed=true";
        }
    }

}