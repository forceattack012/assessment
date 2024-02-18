package com.kbtg.bootcamp.posttest.users.repository;

import com.kbtg.bootcamp.posttest.users.entity.GetAllTicketByUserIdDTO;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTicketRepository extends JpaRepository<UserTicket, Long> {
  @Query(
      value =
          "SELECT ut.ticket AS ticket, l.price AS price"
              + " FROM "
              + " user_ticket ut "
              + " left join lotteries l on l.ticket = ut.ticket"
              + " where ut.user_id = ?",
      nativeQuery = true)
  List<GetAllTicketByUserIdDTO> findAllTicketByUserId(String userId);

  @Query(value = "SELECT * FROM user_ticket WHERE user_id = ? AND ticket = ?", nativeQuery = true)
  List<UserTicket> findByUserIdAndTicket(String userId, String ticket);

  @Modifying
  @Transactional
  @Query(
      value = "DELETE FROM user_ticket ut WHERE ut.user_id = ? AND ut.ticket = ?",
      nativeQuery = true)
  void deleteByUserIdAndTicket(String userId, String ticket);
}
