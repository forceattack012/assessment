package com.kbtg.bootcamp.posttest.users.repository;

import com.kbtg.bootcamp.posttest.users.entity.GetAllTicketByUserIdDTO;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
