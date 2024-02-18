package com.kbtg.bootcamp.posttest.lotteries.model;

import jakarta.validation.constraints.*;

public record TicketRequestDTO(
    @NotBlank(message = "is required")
        @NotNull(message = "is required")
        @Size(min = 6, max = 6, message = "must be 6 characters only")
        @Pattern(regexp = "[0-9]+", message = "must be number only")
        String ticket,
    @Positive(message = "is positive number") int price,
    @Positive(message = "is positive number") int amount) {}
