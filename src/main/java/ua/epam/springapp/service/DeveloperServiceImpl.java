package ua.epam.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Developer;
import ua.epam.springapp.repository.DeveloperRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepo;

    private final DeveloperMapper developerMapper;

    @Autowired
    public DeveloperServiceImpl(DeveloperRepository developerRepo, DeveloperMapper developerMapper) {
        this.developerRepo = developerRepo;
        this.developerMapper = developerMapper;
    }

    @Override
    public void add(DeveloperDto developerDto) {
        Developer developer = developerMapper.fromDto(developerDto);
        developerRepo.add(developer);
    }

    @Override
    @Transactional(readOnly = true)
    public DeveloperDto get(UUID id) {
        Developer developer = developerRepo.get(id);
        if (developer == null) {
            throw new EntityNotFoundException();
        }
        return developerMapper.toDto(developer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeveloperDto> getAll() {
        List<Developer> list = developerRepo.getAll();
        if (list == null || list.size() == 0) {
            throw new EntityNotFoundException();
        }
        return list.stream().map(developerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean update(UUID id, DeveloperDto developerDto) {
        Developer developer = developerRepo.get(id);
        if (developer == null) {
            throw new EntityNotFoundException();
        }
        Developer newDeveloper = developerMapper.fromDto(developerDto);

        developer.setName(newDeveloper.getName());
        developer.setAccount(newDeveloper.getAccount());
        developer.setSkills(newDeveloper.getSkills());
        //return new Developer!!!
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public DeveloperDto remove(UUID id) {
        Developer developer = developerRepo.remove(id);
        if (developer == null) {
            throw new EntityNotFoundException();
        }
        return developerMapper.toDto(developer);
    }
}
