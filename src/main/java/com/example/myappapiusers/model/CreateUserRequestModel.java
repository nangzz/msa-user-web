package com.example.myappapiusers.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 사용자가 회원가입할 때 필요한 정보들만 있는 객체
 */

@Data
public class CreateUserRequestModel {

    @NotNull
    @Size(min = 2)
    private String firstName;

    @NotNull
    @Size(min = 2)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 2, max = 16)
    private String password;
    // 사용자는 그냥 단순 pwd만 입력하면 되지만 DB에서는 encrypted 해서 넣으니까 이 변환 작업을 위해 Dto를 사용하는 것
}
