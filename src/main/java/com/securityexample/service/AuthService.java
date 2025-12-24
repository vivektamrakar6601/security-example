package com.securityexample.service;

import com.securityexample.dto.APIResponse;
import com.securityexample.dto.UserDto;
import com.securityexample.entity.User;
import com.securityexample.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
         // spring ioc will tell for this passwrdencodr iam able to creat bean
   @Autowired
    private PasswordEncoder en;

    public APIResponse<String> register(UserDto dto){
        if(userRepository.existsByEmail(dto.getEmail())){
            APIResponse<String> response = new APIResponse<>();
            response.setMessage("Registration Failed");
            response.setStatus(500);
            response.setData("User with Email Id exists");
            return response;
        }
        if(userRepository.existsByUsername(dto.getUsername())){
            APIResponse<String> response = new APIResponse<>();
            response.setMessage("Registration Failed");
            response.setStatus(500);
            response.setData("User with Username exists");
            return response;

        }
        User user= new User();
        BeanUtils.copyProperties(dto,user);
        user.setPassword(en.encode(dto.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        APIResponse<String> response = new APIResponse<>();
        response.setMessage("Registration Done");
        response.setStatus(201);
        response.setData("User is registered");

        return response;
    }

}
