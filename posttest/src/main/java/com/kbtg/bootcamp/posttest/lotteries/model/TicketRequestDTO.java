package com.kbtg.bootcamp.posttest.lotteries.model;

import jakarta.validation.constraints.*;

public record TicketRequestDTO(
    @NotBlank(message = "Ticket is required")
        @NotNull(message = "Ticket is required")
        @Size(min = 6, max = 6, message = "Ticket must be 6 characters only")
        @Pattern(regexp = "[0-9]+", message = "Ticket must be number only")
        String ticket,
    @Positive(message = "Price is positive number") int price,
    @Positive(message = "Amount is positive number") int amount) {}
