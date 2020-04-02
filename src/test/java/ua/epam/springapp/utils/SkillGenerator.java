package ua.epam.springapp.utils;

import ua.epam.springapp.model.Skill;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkillGenerator {

    public static List<Skill> generateSkills(int count) {
        return IntStream.range(0, count)
                .mapToObj(SkillGenerator::generateSingleSkill)
                .collect(Collectors.toList());
    }

    public static Skill generateSingleSkill(int count) {
        UUID id = UUID.randomUUID();
        String name = "testSkill" + count;
        return new Skill(id, name);
    }

    public static Skill generateSingleSkill() {
        UUID id = UUID.randomUUID();
        String name = "testSkill";
        return new Skill(id, name);
    }
}
