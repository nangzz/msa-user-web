package com.example.myappapiusers.dto;

import lombok.Data;

/**
 * 데이터 넘길 때 사용하며 적절한 타입으로 포맷팅해주는 정보 객체
 */

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email; // 어플리케이션 아이디
    private String password;

    private String userId; // 고유 아이디
    private String encryptedPassword; // 사용자 입력은 단순 pwd (따라서 CreateUserRequestModel 에서 단순 pwd를 받음)
}
