package com.kbtg.bootcamp.posttest.lotteries.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketListResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketNameResponseDTO;
import com.kbtg.bootcamp.posttest.lotteries.model.TicketRequestDTO;
import com.kbtg.bootcamp.posttest.lotteries.service.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void testCreateLottery() throws Exception {
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
    @DisplayName("should get ticket list amount more than zero and return status 200")
    public void testGetTicketListAmountMoreThanZero() throws Exception {
        TicketListResponseDTO ticketListResponseDTO = new TicketListResponseDTO(List.of("000001","000002","123456"));
        when(lotteryService.findTicketsAmountMoreThanZero()).thenReturn(ticketListResponseDTO);

        mockMvc.perform(get("/lotteries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tickets", is(ticketListResponseDTO.tickets())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should get ticket list empty when tickets do not have amount more than zeo")
    public void testGetTicketsEmpty() throws Exception {
        TicketListResponseDTO ticketListResponseDTO = new TicketListResponseDTO(List.of());
        when(lotteryService.findTicketsAmountMoreThanZero()).thenReturn(ticketListResponseDTO);

        mockMvc.perform(get("/lotteries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tickets", hasSize(0)))
                .andExpect(jsonPath("$.tickets", is(ticketListResponseDTO.tickets())))
                .andExpect(status().isOk());
    }
}
