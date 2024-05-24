package com.m295.lb.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Lending")
public class Lending {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Lending_ID")
    private Integer lendingId;
    @Column(name = "Name_Borrower")
    private String nameBorrower;

    public Lending(){}

    public Integer getLendingId() {
        return lendingId;
    }

    public void setLendingId(Integer lending) {
        this.lendingId = lending;
    }

    public String getNameBorrower() {
        return nameBorrower;
    }

    public void setNameBorrower(String nameBorrower) {
        this.nameBorrower = nameBorrower;
    }
}
