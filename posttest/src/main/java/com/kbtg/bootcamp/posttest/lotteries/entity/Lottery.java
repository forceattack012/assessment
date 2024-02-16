package com.kbtg.bootcamp.posttest.lotteries.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity(name = "lotteries")
@Data
public class Lottery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 6, max = 6)
    private String ticket;

    @Positive
    private int price;

    @Positive
    private int amount;

    public Lottery(){

    }

    public Lottery(String ticket, int price, int amount) {
        this.ticket = ticket;
        this.price = price;
        this.amount = amount;
    }
}
