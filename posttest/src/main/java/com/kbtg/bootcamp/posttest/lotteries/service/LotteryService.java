package com.kbtg.bootcamp.posttest.lotteries.service;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotteryService {
    private final LotteryRepository lotteryRepository;

    @Autowired
    public LotteryService(LotteryRepository lotteryRepository) {
        this.lotteryRepository = lotteryRepository;
    }

    @Transactional
    public TicketNameResponseDTO createTicket(TicketRequestDTO ticketRequestDTO){
        Lottery lottery = new Lottery(ticketRequestDTO.ticket(), ticketRequestDTO.price(), ticketRequestDTO.amount());
        try {
            this.lotteryRepository.save(lottery);
        }
        catch (Exception ex){
           throw new InternalServerException(ex.getMessage());
        }

        return new TicketNameResponseDTO(lottery.getTicket());
    }
}
