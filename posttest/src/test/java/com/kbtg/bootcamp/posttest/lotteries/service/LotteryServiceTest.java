package com.kbtg.bootcamp.posttest.lottery.service;

import com.kbtg.bootcamp.posttest.lottery.entity.Ticket;
import com.kbtg.bootcamp.posttest.lottery.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lottery.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lottery.repository.TicketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpServerErrorException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {
    @InjectMocks
    private LotteryService lotteryService;
    @Mock
    private TicketRepository ticketRepository;

    @Test
    @DisplayName("should create a ticket successfully and return the ticket number")
    public void testCreateTicket(){
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
        Ticket expectedTicket = new Ticket("123456", 80, 1);

        when(ticketRepository.save(expectedTicket)).thenReturn(expectedTicket);

        TicketNameResponseDTO actualTicket = lotteryService.create(ticketRequestDTO);

        Assertions.assertEquals(expectedTicket.getTicket(), actualTicket.ticket());

        verify(ticketRepository.save(expectedTicket), times(1));
    }

    @Test
    @DisplayName("should be fail because you create a ticket already exits")
    public void testCreateDuplicateTicket(){
        when(ticketRepository.save(any())).thenThrow(new InternalServerException("ticket has already exits"));

        when(InternalServerException.class, () -> lotteryService.create(ticketRequestDTO), "ticket has already exits");
        verify(ticketRepository.save(any()), times(1));
    }
}
