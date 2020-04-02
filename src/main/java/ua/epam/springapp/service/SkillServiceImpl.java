package ua.epam.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Skill;
import ua.epam.springapp.repository.SkillRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepo;

    private final SkillMapper skillMapper;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepo, SkillMapper skillMapper) {
        this.skillRepo = skillRepo;
        this.skillMapper = skillMapper;
    }

    @Override
    public void add(SkillDto skillDto) {
        Skill skill = skillMapper.fromDto(skillDto);
        skillRepo.add(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDto get(UUID id) {
        Skill skill = skillRepo.get(id);
        if (skill == null) {
            throw new EntityNotFoundException();
        }
        return skillMapper.toDto(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getAll() {
        List<Skill> list = skillRepo.getAll();
        if (list == null || list.size() == 0) {
            throw new EntityNotFoundException();
        }
        return list.stream().map(skillMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean update(UUID id, SkillDto skillDto) {
        Skill skill = skillRepo.get(id);
        if (skill == null) {
            throw new EntityNotFoundException();
        }
        skill.setName(skillDto.getName());
        //return new dto!!!
        return true;
    }

    @Override
    @Transactional
    public SkillDto remove(UUID id) {
        Skill skill = skillRepo.get(id);
        if (skill == null) {
            throw new EntityNotFoundException();
        }
        skillRepo.remove(id);
        return skillMapper.toDto(skill);
    }
}
