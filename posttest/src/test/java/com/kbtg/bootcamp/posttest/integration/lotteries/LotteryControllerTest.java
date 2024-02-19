package com.kbtg.bootcamp.posttest.integration.lotteries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketListResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.repository.LotteryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class LotteryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LotteryRepository lotteryRepository;
    @Test
    @DisplayName("get all lotteries amount are more than zero")
    public void testGetAllLotteries() throws Exception {
        List<Lottery> lotteries = List.of(
                new Lottery("123456", 80 , 2),
                new Lottery("147258", 80 , 1),
                new Lottery("789412", 80 , 0)
        );

        lotteryRepository.saveAll(lotteries);

        TicketListResponseDTO ticketListResponseDTO = new TicketListResponseDTO(List.of("123456", "147258"));

        this.mockMvc.perform(get("/lotteries"))
                .andExpect(jsonPath("$.tickets", hasSize(2)))
                .andExpect(jsonPath("$.tickets", is(ticketListResponseDTO.tickets())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("create ticket fail because user not admin")
    public void testCreateFailBecauseNotAdmin() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("1", 100, 200);

        this.mockMvc.perform(post("/admin/lotteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("create ticket fail because ticket invalid")
    public void testCreateFail() throws Exception {
        // less than 6 ch
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("1", 100, 200);

        this.mockMvc.perform(post("/admin/lotteries")
                        .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.path", is("uri=/admin/lotteries")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.error_message", is("ticket must be 6 characters only")))
                .andExpect(status().isBadRequest());

        // not number only
        ticketRequestDTO = new TicketRequestDTO("ASXCs1", 100, 200);

        this.mockMvc.perform(post("/admin/lotteries")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(jsonPath("$.error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.path", is("uri=/admin/lotteries")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.error_message", is("ticket must be number only")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("create ticket success and can get lottery 1 list")
    public void testCreateSuccess() throws Exception {
        // create lottery
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 100, 200);
        this.mockMvc.perform(post("/admin/lotteries")
                        .with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(jsonPath("$.ticket", is(ticketRequestDTO.ticket())))
                .andExpect(status().isCreated());

        // get lottery
        TicketListResponseDTO ticketListResponseDTO = new TicketListResponseDTO(List.of("123456"));
        this.mockMvc.perform(get("/lotteries"))
                .andExpect(jsonPath("$.tickets", hasSize(1)))
                .andExpect(jsonPath("$.tickets", is(ticketListResponseDTO.tickets())))
                .andExpect(status().isOk());
    }


    @AfterEach
    public void delete(){
        lotteryRepository.deleteAll();
    }
}
