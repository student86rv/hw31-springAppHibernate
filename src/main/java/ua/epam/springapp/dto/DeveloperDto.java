package ua.epam.springapp.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DeveloperDto {

    private UUID id;

    private String name;

    private Set<SkillDto> skills = new HashSet<>();

    private AccountDto account;

    public DeveloperDto() {
    }

    public DeveloperDto(UUID id, String name, Set<SkillDto> skills, AccountDto account) {
        this.id = id;
        this.name = name;
        this.skills = skills;
        this.account = account;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillDto> skills) {
        this.skills = skills;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "DeveloperDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                ", account=" + account +
                '}';
    }
}
