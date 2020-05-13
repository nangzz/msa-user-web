package com.example.myappapiusers.service;

import com.example.myappapiusers.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService { // UserDetailsService에 있는 메소드를 상속받았으니  UserServiceImpl 클래스에서 UserDetailsService의 메소드가 재정의 돼야 함

    UserDto createUser(UserDto userDetails);
    UserDto getUserDetailsByEmail(String email);
}
