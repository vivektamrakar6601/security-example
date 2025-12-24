package com.securityexample.config;

import com.securityexample.repository.UserRepository;
import com.securityexample.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {
  @Autowired
private JwtService jwtService;
int x=100;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader!=null && authHeader.startsWith("Bearer ")){             //token bearer se start hota h ishliye
                                                                                //ye condition use hua h then only token will go for validation!
            String token = authHeader.substring(7);                   //isse direct 7th index se token print hoga
            String username =jwtService.vlaidateTokenAndRetrieveSubject(token);
                                                                             //ye token ka secret_key jstservcie class k secret_key k match hoga tbhi dcryption hoga
         if( username != null && SecurityContextHolder.getContext().getAuthentication()==null){

             var userDetails = userRepository.findByUsername(username);//iske pass role h from db
             var authToken= new UsernamePasswordAuthenticationToken(
                     userDetails,null, Collections.singleton(new SimpleGrantedAuthority(userDetails.getRole()))
             );
             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// biult in h jo http req se extrea details ko authAToken m set krdeta h
         SecurityContextHolder.getContext().setAuthentication(authToken);//authtoken m ulr+role+userdeatls h jo securtiy.. m set ho rhi h'


         }



        }
       filterChain.doFilter(request,response);

    }
}
