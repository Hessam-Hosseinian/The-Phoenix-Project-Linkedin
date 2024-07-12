package com.nessam.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "direct")
public class Direct {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;


    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private String receiver;


    @Column(name = "LastMessage")
    public String LastMessage;
}
