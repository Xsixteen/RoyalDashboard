package com.eulicny.homedashboard.rest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController {

    @RequestMapping(value="/auth", method = RequestMethod.POST)
    public String login(@RequestAttribute("username") String userName, @RequestAttribute("password")  String password) {

        //does the authentication
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "index";
    }
}
