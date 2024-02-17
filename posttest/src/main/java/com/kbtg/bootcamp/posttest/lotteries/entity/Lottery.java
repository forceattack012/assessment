package com.kbtg.bootcamp.posttest.lotteries.entity;

import com.kbtg.bootcamp.posttest.users.entity.UserTicket;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Entity(name = "lotteries")
@Data
public class Lottery {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  @Size(min = 6, max = 6)
  private String ticket;

  @Positive private int price;

  private int amount;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "lottery")
  private List<UserTicket> userTicketList;

  public Lottery() {}

  public Lottery(Long id, String ticket, int price, int amount) {
    this.id = id;
    this.ticket = ticket;
    this.price = price;
    this.amount = amount;
  }

  public Lottery(String ticket, int price, int amount) {
    this.ticket = ticket;
    this.price = price;
    this.amount = amount;
  }
}
