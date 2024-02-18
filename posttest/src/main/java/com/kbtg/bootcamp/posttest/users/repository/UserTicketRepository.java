package com.kbtg.bootcamp.posttest.users.repository;

import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {}
