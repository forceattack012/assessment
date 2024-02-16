package com.kbtg.bootcamp.posttest.lotteries.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LotteryControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private LotteryService lotteryService;
    @BeforeEach
    public void setup(){
        LotteryController lotteryController = new LotteryController(lotteryService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(lotteryController)
                .alwaysDo(print())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("should create successfully a lottery")
    public void createLottery() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("123456", 80, 1);
        TicketNameResponseDTO ticketNameResponseDTO = new TicketNameResponseDTO("123456");

        when(lotteryService.createTicket(ticketRequestDTO)).thenReturn(ticketNameResponseDTO);

        mockMvc.perform(post("/admin/lotteries")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(jsonPath("$.ticket", is(ticketNameResponseDTO.ticket())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("should create successfully a lottery")
    public void createLottery1() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO("1", 0, 0);
        String pathAdmin = "/admin/lotteries";

        mockMvc.perform(post(pathAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
