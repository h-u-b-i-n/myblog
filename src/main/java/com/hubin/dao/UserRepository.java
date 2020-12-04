package com.hubin.dao;

import com.hubin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username, String password);
//    User queryByUsernameAndPassword(@Param("username") String username,
//                                    @Param("password") String password);

}
