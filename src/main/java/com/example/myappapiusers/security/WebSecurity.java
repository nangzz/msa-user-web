package com.example.myappapiusers.security;

import com.example.myappapiusers.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 스프링부트가 빈 찾아서 등록하는 과정에서 이 클래스도 등록됨
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment env;
    private IUserService userService; // 생성자에 추가
    private BCryptPasswordEncoder bCryptPasswordEncoder; // 이미 빈 등록 해놨음 / 생성자에 추가


    @Autowired
    public WebSecurity(Environment env, IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 접속 허용 범위 설정 (/users로 들어오는 모든 접속 허용)
        // http.authorizeRequests().antMatchers("/users/**").permitAll();

        // 모든 사용자 허용
        http.authorizeRequests().antMatchers("/**").permitAll()
                                                                    .and()
                                                                    .addFilter(getAuthenticationFilter());

        // 특정 ip 허용
//        http.authorizeRequests()
//                .antMatchers("/**").hasIpAddress("172.26.146.113")
//                .antMatchers("/**").hasIpAddress("59.29.224.144"); // ip 주소를 하드코딩하지말고 config 파일에 설정하자

//        http.authorizeRequests()
//                .antMatchers("/**").hasIpAddress(env.getProperty("gateway.ip"));

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env, authenticationManager());
        authenticationFilter.setAuthenticationManager(
            authenticationManager()
        );

        return authenticationFilter;
    }

    // 클라이언트 로그인


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService) // 데이터베이스 체크 위해 서비스 넘김 / userDetailsService는 Spring Security의 메소드이기 때문에 프젝에서 사용하려면 UserService 인터페이스가 해당 클래스를 상속한 상태여야 함
                .passwordEncoder(bCryptPasswordEncoder); // userDetailsService는 지정된 이름이며 로그인 시 사용자의 아이디를 검색
    }
}
