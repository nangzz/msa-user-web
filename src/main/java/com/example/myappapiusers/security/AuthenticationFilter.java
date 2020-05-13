package com.example.myappapiusers.security;

import com.example.myappapiusers.dto.UserDto;
import com.example.myappapiusers.model.LoginRequestModel;
import com.example.myappapiusers.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.ribbon.proxy.annotation.Http;
import io.jsonwebtoken.Jwts;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * WebSecurity가 이 클래스 사용
 */

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // DB 데이터 가져오기 위해 서비스 접근
    private IUserService userService;
    private Environment env;

    @Autowired
    public AuthenticationFilter(IUserService userService, Environment env, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class); // 우리가 만든 LoginRequestModel 형태로 바꾸기기

            return getAuthenticationManager().authenticate( // 여기서 DB 데이터와 비교
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(), // 비교해서 email 있는지 비교
                            creds.getPassword(), // 비교해서 pwd 있는지 비교
                            new ArrayList<>()
                    )
            );
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    // 성공적인 로그인 시 작업할 내용 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // authResult에 email 정보 담겨있음(pwd는 내부적으로)
        String email = ((User)authResult.getPrincipal()).getUsername(); // Spring Security 자체에 username이라고 되어있고 그게 email을 의미함
        UserDto userDetail = userService.getUserDetailsByEmail(email); // 인증이 됐지만 인증이 됐다는 정보는 email뿐이니까 그 사람이 유료회원인지 무료회원인지 모른다. 그래서 한번더 서비스 통해서 그 데이터를 가져올거야

        // 사용자 email 가지고 토큰 만드는 부분
        // token expiration time > application.xml에 설정해놓음
        String token = Jwts.builder()

                           .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetail.getUserId());


    }
}
