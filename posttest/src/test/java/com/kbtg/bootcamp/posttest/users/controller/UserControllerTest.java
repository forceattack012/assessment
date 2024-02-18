package com.kbtg.bootcamp.posttest.users.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kbtg.bootcamp.posttest.users.model.ReportLotteriesDTO;
import com.kbtg.bootcamp.posttest.users.model.UserBuyLotteryResponseDTO;
import com.kbtg.bootcamp.posttest.users.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  private MockMvc mockMvc;
  @Mock private UserService userService;

  @BeforeEach
  public void setup() {
    UserController userController = new UserController(userService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
  }

  @Test
  @DisplayName("should buy lottery and return id of transaction")
  public void testBuyTicketSuccess() throws Exception {
    String mockUserId = "0123456789";
    String mockTicket = "123456";
    String mockTransactionId = "2";

    when(userService.buyLottery(mockUserId, mockTicket))
        .thenReturn(new UserBuyLotteryResponseDTO(mockTransactionId));

    String pathUserBuy = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(post(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(mockTransactionId)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("should get all lotteries, amount, price was bought")
  public void testGetAllLotteriesWereBought() throws Exception {
    String mockUserId = "1234567890";
    List<String> mockTickets = List.of("000001", "000002", "123456");
    int cost = 240;
    int count = 3;
    ReportLotteriesDTO reportLotteriesDTO = new ReportLotteriesDTO(mockTickets, count, cost);

    when(userService.reportLotteriesByUserId(mockUserId)).thenReturn(reportLotteriesDTO);

    String urlReport = String.format("/users/%s/lotteries", mockUserId);
    this.mockMvc
        .perform(get(urlReport).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tickets", is(mockTickets)))
        .andExpect(jsonPath("$.count", is(count)))
        .andExpect(jsonPath("$.cost", is(cost)))
        .andExpect(status().isOk());
  }
}
