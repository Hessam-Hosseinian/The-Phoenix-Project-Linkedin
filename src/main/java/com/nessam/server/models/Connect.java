package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Connect")
public class Connect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(name = "person1", nullable = false)
    private String person1;
    @Column(name = "person2", nullable = false)
    private String person2;

    public Connect(String person1, String person2) {
        this.person1 = person1;
        this.person2 = person2;
    }

    public Connect() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson1() {
        return person1;
    }

    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getPerson2() {
        return person2;
    }

    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    @Override
    public String toString() {
        return "Connect{" +
                "id=" + id +
                ", person1='" + person1 + '\'' +
                ", person2='" + person2 + '\'' +
                '}';
    }
}
