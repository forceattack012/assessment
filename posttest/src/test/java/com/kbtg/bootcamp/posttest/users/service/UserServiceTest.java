package com.kbtg.bootcamp.posttest.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.exceptions.NotFoundException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import com.kbtg.bootcamp.posttest.users.entity.User;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import com.kbtg.bootcamp.posttest.users.repository.UserRepository;
import com.kbtg.bootcamp.posttest.users.repository.UserTicketRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks private UserService userService;
  @Mock private UserRepository userRepository;
  @Mock private UserTicketRepository userTicketRepository;
  @Mock private LotteryService lotteryService;

  @Test
  @DisplayName("user can buy lottery with parameters are user id and ticket, and return id")
  public void buyLotteryReturnIdTest() {
    String mockTicket = "123456";
    String mockUserId = "0123456789";
    Lottery mockLottery = new Lottery(1L, mockTicket, 80, 1);
    User mockUser = new User(mockUserId, "John");
    UserTicket mockUserTicket = new UserTicket(mockUser, mockLottery);
    UserTicket mockUserTicketResponse = new UserTicket(2, mockUser, mockLottery);

    when(lotteryService.findLotteryByTicketAmountMoreThanZero(mockTicket)).thenReturn(mockLottery);
    when(lotteryService.update(anyLong(), any())).thenReturn(mockLottery);
    when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
    when(userTicketRepository.save(mockUserTicket)).thenReturn(mockUserTicketResponse);

    String actualId = userService.buyLottery(mockUserId, mockTicket);

    assertEquals(actualId, String.valueOf(mockUserTicketResponse.getId()));
    verify(lotteryService, times(1)).findLotteryByTicketAmountMoreThanZero(mockTicket);
    verify(userRepository, times(1)).findById(mockUserId);

    verify(lotteryService, times(1)).update(anyLong(), any());

    verify(userTicketRepository, times(1)).save(mockUserTicket);
  }

  @Test
  @DisplayName("user cannot buy lottery because lottery not found or amount is 0")
  public void buyFailWhenLotteryNotFoundOrEmpty() {
    String mockTicket = "123456";
    String mockUserId = "0123456789";
    Lottery mockLottery = new Lottery(1L, mockTicket, 80, 1);
    User mockUser = new User(mockUserId, "John");
    UserTicket mockUserTicket = new UserTicket(mockUser, mockLottery);

    when(userRepository.findById(any())).thenReturn(Optional.of(mockUser));
    when(lotteryService.findLotteryByTicketAmountMoreThanZero(mockTicket))
        .thenThrow(new NotFoundException("ticket not found"));

    assertThrows(
        NotFoundException.class,
        () -> userService.buyLottery(mockUserId, mockTicket),
        "ticket not found");

    verify(lotteryService, times(1)).findLotteryByTicketAmountMoreThanZero(mockTicket);
    verify(userRepository, times(1)).findById(mockUserId);

    verify(lotteryService, never()).update(anyLong(), any());

    verify(userTicketRepository, never()).save(mockUserTicket);
  }

  @Test
  @DisplayName("user cannot buy lottery because user not found")
  public void buyFailWhenUserNotFound() {
    String mockTicket = "123456";
    String mockUserId = "0123456789";
    Lottery mockLottery = new Lottery(1L, mockTicket, 80, 1);
    User mockUser = new User(mockUserId, "John");
    UserTicket mockUserTicket = new UserTicket(mockUser, mockLottery);

    when(userRepository.findById(mockUserId)).thenThrow(new NotFoundException("user not found"));

    assertThrows(
        NotFoundException.class,
        () -> userService.buyLottery(mockUserId, mockTicket),
        "user not found");

    verify(userRepository, times(1)).findById(mockUserId);
    verify(lotteryService, times(0)).findLotteryByTicketAmountMoreThanZero(mockTicket);

    verify(lotteryService, never()).update(anyLong(), any());

    verify(userTicketRepository, never()).save(mockUserTicket);
  }

  @Test
  @DisplayName("user cannot buy lottery because update lottery fail")
  public void buyFailWhenUpdateLotteryFail() {
    String mockTicket = "123456";
    String mockUserId = "0123456789";
    Lottery mockLottery = new Lottery(1L, mockTicket, 80, 1);
    User mockUser = new User(mockUserId, "John");
    UserTicket mockUserTicket = new UserTicket(mockUser, mockLottery);

    when(lotteryService.findLotteryByTicketAmountMoreThanZero(mockTicket)).thenReturn(mockLottery);
    when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
    when(lotteryService.update(anyLong(), any()))
        .thenThrow(new InternalServerException("cannot update lottery"));

    assertThrows(
        InternalServerException.class,
        () -> userService.buyLottery(mockUserId, mockTicket),
        "cannot update lottery");

    verify(lotteryService, times(1)).findLotteryByTicketAmountMoreThanZero(mockTicket);
    verify(userRepository, times(1)).findById(mockUserId);

    verify(lotteryService, times(1)).update(anyLong(), any());

    verify(userTicketRepository, never()).save(mockUserTicket);
  }
}
