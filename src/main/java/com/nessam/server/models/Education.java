package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "institution")
    private String institution;

    @Column(name = "degree")
    private String degree;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;


}
