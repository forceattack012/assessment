package com.kbtg.bootcamp.posttest.users.service;

import com.kbtg.bootcamp.posttest.constants.ErrorMessageConstant;
import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.exceptions.NotFoundException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import com.kbtg.bootcamp.posttest.users.entity.GetAllTicketByUserIdDTO;
import com.kbtg.bootcamp.posttest.users.entity.User;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import com.kbtg.bootcamp.posttest.users.model.LotteryResponseDTO;
import com.kbtg.bootcamp.posttest.users.model.ReportLotteriesDTO;
import com.kbtg.bootcamp.posttest.users.model.UserBuyLotteryResponseDTO;
import com.kbtg.bootcamp.posttest.users.repository.UserRepository;
import com.kbtg.bootcamp.posttest.users.repository.UserTicketRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
  public UserBuyLotteryResponseDTO buyLottery(String userId, String ticket) {
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

    return new UserBuyLotteryResponseDTO(id);
  }

  public ReportLotteriesDTO reportLotteriesByUserId(String userId) {
    findUserByUserId(userId);

    List<GetAllTicketByUserIdDTO> getAllTicketByUserIdDTO =
        userTicketRepository.findAllTicketByUserId(userId);

    List<String> tickets =
        getAllTicketByUserIdDTO.stream().map(GetAllTicketByUserIdDTO::getTicket).toList();
    int count = getAllTicketByUserIdDTO.size();
    int totalPrice = calculateTotalPriceOfLotteries(getAllTicketByUserIdDTO);

    return new ReportLotteriesDTO(tickets, count, totalPrice);
  }

  @Transactional
  public LotteryResponseDTO sellLotteryByUserIdAndTicket(String userId, String ticket) {
    List<UserTicket> userTickets = this.userTicketRepository.findByUserIdAndTicket(userId, ticket);
    Optional<Lottery> lotteryOptional =
        userTickets.stream().findFirst().map(UserTicket::getLottery);

    if (lotteryOptional.isEmpty()) {
      throw new NotFoundException(ErrorMessageConstant.LOTTERY_OR_USER_NOT_FOUND);
    }

    Lottery lottery = lotteryOptional.get();

    try {
      int countTickets = userTickets.size();
      int newAmount = lottery.getAmount() + countTickets;
      lottery.setAmount(newAmount);

      this.lotteryService.update(lottery.getId(), lottery);

      this.userTicketRepository.deleteByUserIdAndTicket(userId, ticket);
    } catch (Exception exception) {
      throw new InternalServerException(exception.getMessage());
    }

    return new LotteryResponseDTO(ticket);
  }

  private int calculateTotalPriceOfLotteries(
      List<GetAllTicketByUserIdDTO> getAllTicketByUserIdDTO) {
    return getAllTicketByUserIdDTO.stream()
        .map(GetAllTicketByUserIdDTO::getPrice)
        .mapToInt(Integer::valueOf)
        .sum();
  }

  private User findUserByUserId(String userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorMessageConstant.USER_NOT_FOUND));
  }
}
