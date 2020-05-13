package com.example.myappapiusers.controller;

import com.example.myappapiusers.dto.UserDto;
import com.example.myappapiusers.model.CreateUserRequestModel;
import com.example.myappapiusers.model.CreateUserResponseModel;
import com.example.myappapiusers.repository.IUsersRepository;
import com.example.myappapiusers.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Data
 * CreateUserRequestModel 사용
 */

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    Environment env; // import 주의

    @Autowired
    UserServiceImpl userService;

    // 상태코드 체크하는 메소드
    @GetMapping("/status/check")
    public String status() {

        return String.format("Working on port %s", env.getProperty("local.server.port")); // application.yml에서 포트번호 가져오기기
   }

   // model mapper 라이브러리 사용
   @PostMapping(
           consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
           produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
   )
   public ResponseEntity<CreateUserResponseModel> creatUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
       // CreateUserRequestModel -> UserDto 형식으로 변환할 것(서비스로 전달 위해)


       /*
       아래처럼 해도 되지만 필드가 많아지면 코드가 엄청 길어지므로 model mapping 라이브러리 사용
       UserDto userDto = new UserDto();
       dto.setFirstName(userDetails.getFirstName());
        */

       ModelMapper modelMapper = new ModelMapper();
       modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 타이트하게 토씨하나라도 틀리면 null 변환 안됨
       UserDto userDto = modelMapper.map(userDetails, UserDto.class);
       UserDto createDto = userService.createUser(userDto);

       // return new ResponseEntity(HttpStatus.CREATED);
       CreateUserResponseModel returnValue = modelMapper.map(createDto, CreateUserResponseModel.class);

       return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
   }
}
