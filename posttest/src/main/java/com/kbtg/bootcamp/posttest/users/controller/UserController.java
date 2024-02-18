package com.kbtg.bootcamp.posttest.users.controller;

import com.kbtg.bootcamp.posttest.constants.ErrorMessageConstant;
import com.kbtg.bootcamp.posttest.users.model.LotteryResponseDTO;
import com.kbtg.bootcamp.posttest.users.model.ReportLotteriesDTO;
import com.kbtg.bootcamp.posttest.users.model.UserBuyLotteryResponseDTO;
import com.kbtg.bootcamp.posttest.users.service.UserService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/{userId}/lotteries/{ticketId}")
  public ResponseEntity<UserBuyLotteryResponseDTO> buyLottery(
      @PathVariable("userId")
          @Size(min = 10, max = 10, message = ErrorMessageConstant.USER_ID_MUST_TEN_CH)
          @Pattern(regexp = "[0-9]+", message = ErrorMessageConstant.USER_ID_MUST_NUMBER)
          String userId,
      @PathVariable("ticketId")
          @Size(min = 6, max = 6, message = ErrorMessageConstant.TICKET_ID_MUST_SIX_CH)
          @Pattern(regexp = "[0-9]+", message = ErrorMessageConstant.TICKET_ID_MUST_NUMBER)
          String ticketId) {

    UserBuyLotteryResponseDTO buyLotteryResponseDTO = this.userService.buyLottery(userId, ticketId);

    return ResponseEntity.status(HttpStatus.CREATED).body(buyLotteryResponseDTO);
  }

  @GetMapping("/{userId}/lotteries")
  public ReportLotteriesDTO getReportLotteriesByUserId(
      @PathVariable("userId")
          @Size(min = 10, max = 10, message = ErrorMessageConstant.USER_ID_MUST_TEN_CH)
          @Pattern(regexp = "[0-9]+", message = ErrorMessageConstant.USER_ID_MUST_NUMBER)
          String userId) {
    return this.userService.reportLotteriesByUserId(userId);
  }

  @DeleteMapping("/{userId}/lotteries/{ticketId}")
  public LotteryResponseDTO sellLotteryByUserIdAndTicket(
      @PathVariable("userId")
          @Size(min = 10, max = 10, message = ErrorMessageConstant.USER_ID_MUST_TEN_CH)
          @Pattern(regexp = "[0-9]+", message = ErrorMessageConstant.USER_ID_MUST_NUMBER)
          String userId,
      @PathVariable("ticketId")
          @Size(min = 6, max = 6, message = ErrorMessageConstant.TICKET_ID_MUST_SIX_CH)
          @Pattern(regexp = "[0-9]+", message = ErrorMessageConstant.TICKET_ID_MUST_NUMBER)
          String ticketId) {

    return this.userService.sellLotteryByUserIdAndTicket(userId, ticketId);
  }
}
