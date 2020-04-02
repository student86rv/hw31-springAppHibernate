package ua.epam.springapp.dto;

import java.util.Objects;
import java.util.UUID;

public class AccountDto {

    private UUID id;

    private String email;

    private String status;

    public AccountDto() {
    }

    public AccountDto(UUID id, String email, String status) {
        this.id = id;
        this.email = email;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(status, that.status);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
