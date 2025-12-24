package com.securityexample.controller;

import com.securityexample.dto.APIResponse;
import com.securityexample.dto.LoginDto;
import com.securityexample.dto.UserDto;
import com.securityexample.service.AuthService;
import com.securityexample.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControler {
    @Autowired
    private AuthService authService;
   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private JwtService jwtService;
    //http://localhost:8080/api/v1/auth/signup
    @PostMapping("/signup")
     public ResponseEntity<APIResponse<String>> register(
             @RequestBody UserDto userDto ){

        APIResponse<String> response = authService.register(userDto);
        return  new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
     }

     @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> veryLogin(

            @RequestBody LoginDto loginDto
     ){
         APIResponse<String> responce = new APIResponse<>();
         UsernamePasswordAuthenticationToken
                 token= new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());

        try{
            Authentication authenticate = authenticationManager.authenticate(token);
            if(authenticate.isAuthenticated()){
                String jwtToken = jwtService.genrateToken(loginDto.getUsername(),
                        authenticate.getAuthorities().iterator().next().getAuthority());
                responce.setMessage("login successful");
                responce.setStatus(200);
                responce.setData(jwtToken);
                return new ResponseEntity<>(responce,HttpStatusCode.valueOf(responce.getStatus()));
            }
            responce.setMessage("failed");
            responce.setStatus(401);
            responce.setData("un-authorized access");
            return  new ResponseEntity<>(responce,HttpStatusCode.valueOf(responce.getStatus()));

        }catch (InternalAuthenticationServiceException e){
            System.out.println("user credentials is incorrect ");


        }
         responce.setMessage("failed");
         responce.setStatus(401);
         responce.setData("un-authorized access");
         return  new ResponseEntity<>(responce,HttpStatusCode.valueOf(responce.getStatus()));
     }
   // @GetMapping("/profile")
    //public ResponseEntity<String> profile(
          //  @AuthenticationPrincipal UserDetails userDetails
          //  ){
      //  return  new ResponseEntity<>(userDetails.getUsername(), HttpStatus.OK);
   // }

    @GetMapping("/profile")
    public String profile(Authentication authentication) {
        return authentication.getName(); // logged-in username
    }


}
