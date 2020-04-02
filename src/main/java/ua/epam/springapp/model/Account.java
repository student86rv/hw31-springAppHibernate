package ua.epam.springapp.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(length=16)
    private UUID id = UUID.randomUUID();

    private String email;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToOne(mappedBy = "account")
    private Developer developer;

    public Account() {
    }

    public Account(String email, AccountStatus status) {
        this.email = email;
        this.status = status;
    }

    public Account(UUID id, String email, AccountStatus status) {
        this.id = id;
        this.email = email;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(email, account.email) &&
                status == account.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, status);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
