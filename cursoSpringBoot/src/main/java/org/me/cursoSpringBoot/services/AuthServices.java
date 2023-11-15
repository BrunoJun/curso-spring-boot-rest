package org.me.cursoSpringBoot.services;

import org.me.cursoSpringBoot.data.vo.v1.security.AccountCredentialsVO;
import org.me.cursoSpringBoot.data.vo.v1.security.TokenVO;
import org.me.cursoSpringBoot.repositories.UserRepository;
import org.me.cursoSpringBoot.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DelegatingPasswordEncoder delegatingPasswordEncoder;

    public ResponseEntity signin(AccountCredentialsVO data){

        try {

            var username = data.getUsername();
            var password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUserName(username);
            var tokenResponse = new TokenVO();

            if (user != null){

                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
            } else {

                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception exception) {

            throw new BadCredentialsException("Invalid username or password!");
        }
    }

    public ResponseEntity refreshToken(String username, String refreshToken){

        var user = userRepository.findByUserName(username);
        var tokenResponse = new TokenVO();

        if (user != null){

            tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
        } else {

            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        return ResponseEntity.ok(tokenResponse);
    }
}
