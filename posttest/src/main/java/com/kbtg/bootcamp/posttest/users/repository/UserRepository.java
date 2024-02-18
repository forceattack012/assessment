package com.kbtg.bootcamp.posttest.users.repository;

import com.kbtg.bootcamp.posttest.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {}
