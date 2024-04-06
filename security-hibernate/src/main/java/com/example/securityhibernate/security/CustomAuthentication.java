package com.example.securityhibernate.security;

import com.example.securityhibernate.service.imp.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthentication implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Autowired
    UsersServiceImp usersServiceImp;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("kiemtra "+username+password);
        if(usersServiceImp.checkLogin(username, password)){
            UserDetails userDetails = userService.loadUserByUsername(username);

            return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
