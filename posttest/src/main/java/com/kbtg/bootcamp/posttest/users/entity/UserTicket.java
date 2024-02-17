package com.kbtg.bootcamp.posttest.users.entity;

import com.kbtg.bootcamp.posttest.lotteries.entity.Lottery;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "user_ticket")
@Data
public class UserTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket", referencedColumnName = "ticket")
    private Lottery lottery;

    public UserTicket(){

    }
    public UserTicket(long id, User user, Lottery lottery) {
        this.id = id;
        this.user = user;
        this.lottery = lottery;
    }

    public UserTicket(User user, Lottery lottery) {
        this.user = user;
        this.lottery = lottery;
    }
}
