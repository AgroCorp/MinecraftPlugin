package me.sativus.testplugin.DAO;

import jakarta.persistence.*;

@Entity
public class Wallet {
    @Id
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", unique = true, nullable = false)
    private User user;

    @Column
    private double balance;

    public Wallet() {
    }

    public Wallet(User user, double balance) {
        this.user = user;
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
