package com.kbtg.bootcamp.posttest.users.service;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.exceptions.NotFoundException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import com.kbtg.bootcamp.posttest.users.entity.User;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import com.kbtg.bootcamp.posttest.users.repository.UserRepository;
import com.kbtg.bootcamp.posttest.users.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final LotteryService lotteryService;
  private final UserRepository userRepository;
  private final UserTicketRepository userTicketRepository;

  @Autowired
  public UserService(
      LotteryService lotteryService,
      UserRepository userRepository,
      UserTicketRepository userTicketRepository) {
    this.lotteryService = lotteryService;
    this.userRepository = userRepository;
    this.userTicketRepository = userTicketRepository;
  }

  @Transactional
  public String buyLottery(String userId, String ticket) {
    String id;

    try {
      User user = findUserByUserId(userId);

      Lottery lottery = lotteryService.findLotteryByTicketAmountMoreThanZero(ticket);

      int reduceAmount = lottery.getAmount() - 1;
      lottery.setAmount(reduceAmount);
      lottery = lotteryService.update(lottery.getId(), lottery);

      UserTicket userTicket = new UserTicket();
      userTicket.setUser(user);
      userTicket.setLottery(lottery);
      userTicket = userTicketRepository.save(userTicket);

      id = String.valueOf(userTicket.getId());
    } catch (NotFoundException notFoundException) {
      throw new NotFoundException(notFoundException.getMessage());
    } catch (Exception ex) {
      throw new InternalServerException(ex.getMessage());
    }

    return id;
  }

  private User findUserByUserId(String userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("user not found"));
  }
}
