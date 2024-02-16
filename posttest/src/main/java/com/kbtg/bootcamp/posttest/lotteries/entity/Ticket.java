package com.kbtg.bootcamp.posttest.lottery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "tickets")
@Data
public class Ticket {
    @Id
    private String ticket;
    private int price;
    private int amount;

    public Ticket(){

    }

    public Ticket(String ticket, int price, int amount) {
        this.ticket = ticket;
        this.price = price;
        this.amount = amount;
    }
}
