package ua.epam.springapp.dto;

import java.util.Objects;
import java.util.UUID;

public class SkillDto {

    private UUID id;

    private String name;

    public SkillDto() {
    }

    public SkillDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillDto skillDto = (SkillDto) o;
        return Objects.equals(id, skillDto.id) &&
                Objects.equals(name, skillDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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

    @Override
    public String toString() {
        return "SkillDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
