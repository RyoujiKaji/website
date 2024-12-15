package com.example.demo.models.General;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "general")

public class General {
    @Id
    @Column(name = "id")
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public General() {
    };

    public General(int id, int visits) {
        this.id = id;
        this.visits = visits;
    }

    @Column(name = "visits")
    // @Type(type = "text")
    private int visits;

    public Integer getId() {
        return id;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setId(Integer _id) {
        this.id = _id;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }
}
