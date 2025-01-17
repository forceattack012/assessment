package com.kbtg.bootcamp.posttest.lotteries.controller;

import com.kbtg.bootcamp.posttest.lotteries.model.TicketListResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LotteryController {
  private final LotteryService lotteryService;

  public LotteryController(LotteryService lotteryService) {
    this.lotteryService = lotteryService;
  }

  @PostMapping("/admin/lotteries")
  @SecurityRequirement(name = "basicAuth")
  public ResponseEntity<TicketNameResponseDTO> createTicket(
      @RequestBody @Validated TicketRequestDTO ticketRequestDTO) {
    TicketNameResponseDTO ticketNameResponseDTO =
        this.lotteryService.createTicket(ticketRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(ticketNameResponseDTO);
  }

  @GetMapping("/lotteries")
  public TicketListResponseDTO findLotteries() {
    return this.lotteryService.findTicketsAmountMoreThanZero();
  }
}
