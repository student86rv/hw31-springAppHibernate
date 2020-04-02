package ua.epam.springapp.service;

import org.springframework.stereotype.Service;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.model.Skill;

import java.util.UUID;

@Service
public class SkillMapper {

    public SkillDto toDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(skill.getId());
        skillDto.setName(skill.getName());
        return skillDto;
    }

    public Skill fromDto(SkillDto skillDto) {
        Skill skill = new Skill();
        UUID dtoId = skillDto.getId();
        if (dtoId != null) {
            skill.setId(dtoId);
        }
        skill.setName(skillDto.getName());
        return skill;
    }
}
