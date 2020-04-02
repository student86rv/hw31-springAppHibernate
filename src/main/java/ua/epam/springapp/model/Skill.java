package ua.epam.springapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @Column(length = 16)
    private UUID id = UUID.randomUUID();

    private String name;

    @ManyToMany(mappedBy = "skills")
    private Set<Developer> developers = new HashSet<>();

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
    }

    public Skill(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Skill skill = (Skill) o;
//        return id == skill.id &&
//                Objects.equals(name, skill.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(id, skill.id) &&
                Objects.equals(name, skill.name);
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

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
