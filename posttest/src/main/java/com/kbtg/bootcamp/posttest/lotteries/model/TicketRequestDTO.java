package com.kbtg.bootcamp.posttest.lottery.model;

public record TicketRequestDTO(
        String ticket,
        int price,
        int amount
) {
}
