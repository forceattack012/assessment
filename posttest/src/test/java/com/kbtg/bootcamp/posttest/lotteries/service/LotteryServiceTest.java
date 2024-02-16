package com.kbtg.bootcamp.posttest.lotteries.service;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {
    @InjectMocks
    private LotteryService lotteryService;
    @Mock
    private LotteryRepository lotteryRepository;

    @Test
    @DisplayName("should create a ticket successfully and return the ticket number")
    public void testCreateTicket(){
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
        Lottery expectedLottery = new Lottery("123456", 80, 1);

        when(lotteryRepository.save(expectedLottery)).thenReturn(expectedLottery);

        TicketNameResponseDTO actualTicket = lotteryService.createTicket(ticketRequestDTO);

        assertEquals(expectedLottery.getTicket(), actualTicket.ticket());

        verify(lotteryRepository, times(1)).save(expectedLottery);
    }

    @Test
    @DisplayName("should be fail because you create a ticket already exits")
    public void testCreateDuplicateTicket(){
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
        when(lotteryRepository.save(any())).thenThrow(new InternalServerException("ticket has already exits"));

        assertThrows(InternalServerException.class, () -> lotteryService.createTicket(ticketRequestDTO), "ticket has already exits");
        verify(lotteryRepository, times(1)).save(any());
    }
}
