package com.kbtg.bootcamp.posttest.lottery.service;

import com.kbtg.bootcamp.posttest.lottery.entity.Ticket;
import com.kbtg.bootcamp.posttest.lottery.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lottery.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lottery.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryService {
    private final TicketRepository ticketRepository;

    @Autowired
    public LotteryService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketNameResponseDTO create(TicketRequestDTO ticketRequestDTO){
        Ticket ticket = new Ticket(ticketRequestDTO.ticket(), ticketRequestDTO.price(), ticketRequestDTO.amount());
        this.ticketRepository.save(ticket);

        return new TicketNameResponseDTO(ticket.getTicket());
    }
}
