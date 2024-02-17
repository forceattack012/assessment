package com.kbtg.bootcamp.posttest.lotteries.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.exceptions.NotFoundException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketListResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {
  @InjectMocks private LotteryService lotteryService;
  @Mock private LotteryRepository lotteryRepository;

  @Test
  @DisplayName("should create a ticket successfully and return the ticket number")
  public void testCreateTicket() {
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
    Lottery expectedLottery = new Lottery("123456", 80, 1);

    when(lotteryRepository.save(expectedLottery)).thenReturn(expectedLottery);

    TicketNameResponseDTO actualTicket = lotteryService.createTicket(ticketRequestDTO);

    assertEquals(expectedLottery.getTicket(), actualTicket.ticket());

    verify(lotteryRepository, times(1)).save(expectedLottery);
  }

  @Test
  @DisplayName("should be fail because you create a ticket already exits")
  public void testCreateDuplicateTicket() {
    TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
    when(lotteryRepository.save(any()))
        .thenThrow(new InternalServerException("ticket has already exits"));

    assertThrows(
        InternalServerException.class,
        () -> lotteryService.createTicket(ticketRequestDTO),
        "ticket has already exits");
    verify(lotteryRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("should be find tickets which have amount more than zero")
  public void TestFindTicketsAmountMoreThanZero() {
    List<Lottery> mockLotteries =
        List.of(
            new Lottery("000001", 80, 1),
            new Lottery("000003", 80, 2),
            new Lottery("000004", 80, 0),
            new Lottery("000005", 80, 0),
            new Lottery("123456", 80, 3));
    when(lotteryRepository.findAll()).thenReturn(mockLotteries);

    TicketListResponseDTO expectedLotteries =
        new TicketListResponseDTO(List.of("000001", "000003", "123456"));

    TicketListResponseDTO actualLotteries = this.lotteryService.findTicketsAmountMoreThanZero();

    assertEquals(expectedLotteries.tickets(), actualLotteries.tickets());
  }

  @Test
  @DisplayName("should be find tickets empty list if the tickets all amount are zero")
  public void TestFindTicketsEmpty() {
    List<Lottery> mockLotteries =
        List.of(
            new Lottery("000001", 80, 0),
            new Lottery("000003", 80, 0),
            new Lottery("000004", 80, 0),
            new Lottery("000005", 80, 0),
            new Lottery("123456", 80, 0));
    when(lotteryRepository.findAll()).thenReturn(mockLotteries);

    TicketListResponseDTO expectedLotteries = new TicketListResponseDTO(List.of());

    TicketListResponseDTO actualLotteries = this.lotteryService.findTicketsAmountMoreThanZero();

    assertEquals(actualLotteries.tickets().size(), 0);
    assertEquals(expectedLotteries.tickets(), actualLotteries.tickets());
  }

  @Test
  @DisplayName("should find lottery by ticket and amount more than zero")
  public void testFindLotteryByTicket() {
    String mockTicket = "123456";
    Lottery mockLottery = new Lottery(mockTicket, 10, 80);

    when(lotteryRepository.findByTicket(mockTicket)).thenReturn(Optional.of(mockLottery));

    Lottery actualLottery = lotteryService.findLotteryByTicketAmountMoreThanZero(mockTicket);

    assertEquals(actualLottery.getTicket(), mockLottery.getTicket());
    assertEquals(actualLottery.getPrice(), mockLottery.getPrice());
    assertEquals(actualLottery.getAmount(), mockLottery.getAmount());
  }

  @Test
  @DisplayName("should find lottery by ticket fail because sold out")
  public void testFindLotteryByTicketFailBecauseSoldOut() {
    String mockTicket = "123456";
    Lottery mockLottery = new Lottery(mockTicket, 10, 0);

    when(lotteryRepository.findByTicket(mockTicket)).thenReturn(Optional.of(mockLottery));

    assertThrows(
        NotFoundException.class,
        () -> lotteryService.findLotteryByTicketAmountMoreThanZero(mockTicket),
        "ticket sold out");
  }
}
