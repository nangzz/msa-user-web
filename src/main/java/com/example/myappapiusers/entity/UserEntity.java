package com.example.myappapiusers.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * DB와 직접 연관되는 정보 객체
 * 이 하나로 모델을 다 사용하면 안보여도 될 정보까지 보여지게 되므로 각 용도에 따라 모델 객체 사용
 */
@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPassword;


}
