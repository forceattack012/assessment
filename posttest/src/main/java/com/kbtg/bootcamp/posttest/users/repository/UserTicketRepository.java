package com.kbtg.bootcamp.posttest.users.repository;

import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {
}
