package com.kbtg.bootcamp.posttest.integration.users;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kbtg.bootcamp.posttest.exceptions.ApiErrorResponse;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import com.kbtg.bootcamp.posttest.users.entity.User;
import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import com.kbtg.bootcamp.posttest.users.repository.UserRepository;
import com.kbtg.bootcamp.posttest.users.repository.UserTicketRepository;
import com.kbtg.bootcamp.posttest.users.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private UserTicketRepository userTicketRepository;
  @Autowired private UserService userService;
  @Autowired private LotteryRepository lotteryRepository;
  @Mock private UserService userServiceMock;

  @BeforeEach
  public void setup() {
    User john = new User("0123456789", "john");
    this.userRepository.save(john);

    Lottery lottery = new Lottery(1L, "123456", 100, 1);
    this.lotteryRepository.save(lottery);
  }

  @Test
  @DisplayName("user buy lottery success then return transaction id")
  public void createLotterySuccess() throws Exception {
    String mockUserId = "0123456789";
    String mockTicket = "123456";
    String actualId = "1";

    String pathUserBuy = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(post(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(actualId)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("user cannot buy lottery because ticket not found or user not found")
  public void createLotteryFailBecauseNotFound() throws Exception {
    String mockUserId = "9988774412";
    String mockTicket = "123456";

    String pathUserBuy = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(post(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is(HttpStatus.NOT_FOUND.getReasonPhrase())))
        .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())))
        .andExpect(jsonPath("$.path", is("uri=" + pathUserBuy)))
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.error_message", is("user not found")))
        .andExpect(status().isNotFound());

    mockUserId = "0123456789";
    mockTicket = "777555";
    pathUserBuy = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);

    ApiErrorResponse apiErrorResponse =
        new ApiErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            "ticket not found",
            "uri=" + pathUserBuy);

    this.mockMvc
        .perform(post(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is(apiErrorResponse.getError())))
        .andExpect(jsonPath("$.status", is(apiErrorResponse.getStatus())))
        .andExpect(jsonPath("$.path", is(apiErrorResponse.getPath())))
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.error_message", is(apiErrorResponse.getErrorMessage())))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("user cannot buy lottery because input invalid")
  public void createLotteryFailBecauseInputInvalid() throws Exception {
    String mockUserId = "1";
    String mockTicket = "1";

    String pathUserBuy = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(post(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
        .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
        .andExpect(jsonPath("$.path", is("uri=" + pathUserBuy)))
        .andExpect(jsonPath("$.timestamp", notNullValue()))
        .andExpect(jsonPath("$.error_message", notNullValue()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("user get history lottery")
  public void userGetHistoryLottery() throws Exception {
    String mockUserId = "0123456789";

    UserTicket userTicket = new UserTicket();
    User john = new User("0123456789", "john");
    Lottery lottery1 = new Lottery(1L, "123456", 100, 1);

    userTicket.setId(1);
    userTicket.setLottery(lottery1);
    userTicket.setUser(john);
    this.userTicketRepository.save(userTicket);

    String getLotteries = String.format("/users/%s/lotteries", mockUserId);
    this.mockMvc
        .perform(get(getLotteries).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tickets", is(List.of("123456"))))
        .andExpect(jsonPath("$.count", is(1)))
        .andExpect(jsonPath("$.cost", is(100)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("user delete lottery")
  public void deleteLotteryWasBought() throws Exception {
    String mockUserId = "0123456789";
    String mockTicket = "123456";

    String getLotteries = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(post(getLotteries).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    String pathSellLottery = String.format("/users/%s/lotteries/%s", mockUserId, mockTicket);
    this.mockMvc
        .perform(delete(pathSellLottery).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lottery", is(mockTicket)))
        .andExpect(status().isOk());

    String pathUserBuy = String.format("/users/%s/lotteries", mockUserId);
    this.mockMvc
        .perform(get(pathUserBuy).contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tickets", is(List.of())))
        .andExpect(jsonPath("$.count", is(0)))
        .andExpect(jsonPath("$.cost", is(0)))
        .andExpect(status().isOk());
  }

  @AfterEach
  public void TerraDown() {
    this.userTicketRepository.deleteAll();
    this.lotteryRepository.deleteAll();
    this.userRepository.deleteAll();
  }
}
