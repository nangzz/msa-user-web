package com.example.myappapiusers.service;

import com.example.myappapiusers.dto.UserDto;
import com.example.myappapiusers.entity.UserEntity;
import com.example.myappapiusers.repository.IUsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Data
 * UserDto 사용
 */

@Service
public class UserServiceImpl implements IUserService {

    /* 서비스 클래스 안에서는 필드 주입 법을 권장하지 않음
    @Autowired
    IUsersRepository usersRepository;
     */

    IUsersRepository usersRepository;

    // 비밀번호 인코딩 저장을 위해 사용용
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // 생성자 주입 법 이용
    @Autowired
    public UserServiceImpl(IUsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    // model mapper 라이브러리 사용
    @Override
    public UserDto createUser(UserDto userDetails) {
        // UserDto -> UserEntity로 변환할 것 (최종 DB로 가기 전 단계)

        userDetails.setUserId(UUID.randomUUID().toString()); // 고유 아이디 생성 코드

        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
//        userEntity.setEncryptedPassword("test encrypted password");

        usersRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    // 사용자의 아이디(이메일)가 있는지 없는지 검사해주는 메소드 (ctrl+o)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(email);
        // 원래 기본 메소드는 findById이지만 email로 찾을거니까 findByEmail로 만들고 메소드를 IUserRepository에 따로 만들어주기

        // 그 사용자는 없습니다.
        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // 그 사용자는 있습니다.
//        return userEntity;
//       우리는 userEntity로 반환할건데 이 메소드 반환타입은 UserDetails니까 변환시키자
        return new User(userEntity.getEmail() /*DB에서 검색되어진 데이터*/, userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>()/*role값*/);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // UserEntity -> UserDto로 변환 (using ModelMapper)
        return new ModelMapper().map(userEntity, UserDto.class); // 간단한 방법
    }
}
