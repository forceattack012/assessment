package com.kbtg.bootcamp.posttest.lotteries.repository;

import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {
    Optional<Lottery> findByTicket(String ticket);


}
