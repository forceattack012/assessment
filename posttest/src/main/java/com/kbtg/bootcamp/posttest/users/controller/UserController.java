package com.kbtg.bootcamp.posttest.users.controller;

import com.kbtg.bootcamp.posttest.users.service.UserService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/{userId}/lotteries/{ticketId}")
  public String buyLottery(
      @Validated
          @PathVariable("userId")
          @Size(min = 10, max = 10, message = "userId must be at least 6 characters")
          @Pattern(regexp = "[0-9]+")
          String userId,
      @PathVariable("ticketId")
          @Size(min = 6, max = 6, message = "ticketId must be at least 6 characters")
          @Pattern(regexp = "[0-9]+")
          String ticketId) {

    return this.userService.buyLottery(userId, ticketId);
  }
}
