package com.example.myappapiusers.model;

import lombok.Data;

/**
 * 요청 데이터와 응답 데이터를 다르게 하기 위한 클래스
 */

@Data
public class CreateUserResponseModel {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;

}
