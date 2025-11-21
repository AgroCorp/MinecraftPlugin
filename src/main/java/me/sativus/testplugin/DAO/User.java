package me.sativus.testplugin.DAO;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {
    @Id
    private UUID uuid;

    @Column
    private String name;
    @Column
    private String password;
    @Column
    private Boolean loggedIn;
    @Column
    private LocalDateTime lastLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    public User() {
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public User(UUID uuid, String name, Wallet wallet) {
        this.uuid = uuid;
        this.name = name;
        this.wallet = wallet;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", loggedIn=" + loggedIn +
                ", wallet=" + wallet +
                '}';
    }
}

