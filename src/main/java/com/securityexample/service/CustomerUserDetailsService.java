package com.securityexample.service;

import com.securityexample.entity.User;
import com.securityexample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service//this steps are fixed in spring security
public class CustomerUserDetailsService implements UserDetailsService {//ye built-in interface we have ti impelment.
    @Autowired// interview
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userRepository.findByUsername(username);//ye db se comfirm krega ki username exist krta hia ni_++
        return new org.springframework.security.core.userdetails.User(userInfo.getUsername(),userInfo.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userInfo.getRole())));

    }
}
