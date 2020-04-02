package ua.epam.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.model.Account;
import ua.epam.springapp.model.Developer;
import ua.epam.springapp.model.Skill;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeveloperMapper {

    private final AccountMapper accountMapper;

    private final SkillMapper skillMapper;

    @Autowired
    public DeveloperMapper(AccountMapper accountMapper, SkillMapper skillMapper) {
        this.accountMapper = accountMapper;
        this.skillMapper = skillMapper;
    }

    public DeveloperDto toDto(Developer developer) {
        DeveloperDto developerDto = new DeveloperDto();
        developerDto.setId(developer.getId());
        developerDto.setName(developer.getName());

        AccountDto accountDto = accountMapper.toDto(developer.getAccount());
        developerDto.setAccount(accountDto);

        Set<SkillDto> skillDtos = developer.getSkills().stream().map(skillMapper::toDto).collect(Collectors.toSet());
        developerDto.setSkills(skillDtos);

        return developerDto;
    }

    public Developer fromDto(DeveloperDto developerDto) {
        Developer developer = new Developer();
        UUID dtoId = developerDto.getId();
        if (dtoId != null) {
            developer.setId(dtoId);
        }
        developer.setName(developerDto.getName());

        Account account = accountMapper.fromDto(developerDto.getAccount());
        developer.setAccount(account);
        account.setDeveloper(developer);

        Set<Skill> skills = developerDto.getSkills().stream().map(skillMapper::fromDto).collect(Collectors.toSet());
        developer.getSkills().addAll(skills);
        for (Skill skill : skills) {
            skill.getDevelopers().add(developer);
        }

        return developer;
    }
}
