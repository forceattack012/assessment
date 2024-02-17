package com.kbtg.bootcamp.posttest.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity(name = "users")
@Data
public class User {

    @Id
    @Pattern(regexp = "[0-9]+")
    @Size(min = 10, max = 10)
    @Column(name = "user_id")
    private String userId;

    @NotBlank
    @NotEmpty
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserTicket> userTicketList;

    public User(){

    }

    public User(String userId, String name){
        this.userId = userId;
        this.name = name;
    }
}
