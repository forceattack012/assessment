package com.kbtg.bootcamp.posttest.lotteries.service;

import com.kbtg.bootcamp.posttest.exceptions.InternalServerException;
import com.kbtg.bootcamp.posttest.exceptions.NotFoundException;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketListResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public TicketNameResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
    Lottery lottery =
        new Lottery(ticketRequestDTO.ticket(), ticketRequestDTO.price(), ticketRequestDTO.amount());
    try {
      this.lotteryRepository.save(lottery);
    } catch (Exception ex) {
      throw new InternalServerException(ex.getMessage());
    }

    return new TicketNameResponseDTO(lottery.getTicket());
  }

  public TicketListResponseDTO findTicketsAmountMoreThanZero() {

    List<String> tickets =
        this.lotteryRepository.findAll().stream()
            .filter(l -> l.getAmount() > 0)
            .map(Lottery::getTicket)
            .collect(Collectors.toList());

    return new TicketListResponseDTO(tickets);
  }

  public Lottery findLotteryByTicketAmountMoreThanZero(String ticket) {
    Optional<Lottery> lotteryOptional = this.lotteryRepository.findByTicket(ticket);

    if (lotteryOptional.isEmpty()) {
      throw new NotFoundException("ticket not found");
    }

    Lottery lottery = lotteryOptional.get();
    boolean isSoldOut = lottery.getAmount() == 0;
    if (isSoldOut) {
      throw new NotFoundException("ticket sold out");
    }

    return lottery;
  }

  @Transactional
  public Lottery update(long id, Lottery lottery) {
    Optional<Lottery> lotteryOptional = lotteryRepository.findById(id);

    if (lotteryOptional.isEmpty()) {
      throw new NotFoundException("lottery not found");
    }

    return this.lotteryRepository.save(lottery);
  }
}
