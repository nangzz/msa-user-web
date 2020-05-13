package com.example.myappapiusers.repository;

import com.example.myappapiusers.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IUsersRepository extends CrudRepository<UserEntity, Long> {

     UserEntity findByEmail(String email);
     // findBy 다음에 실제 컬럼명을 적어야 함(select * from users where email=?)

}
