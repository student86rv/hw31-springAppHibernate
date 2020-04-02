package ua.epam.springapp.service;

import org.junit.Test;
import org.mockito.Mockito;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.exception.EntityNotFoundException;
import ua.epam.springapp.model.Developer;
import ua.epam.springapp.repository.DeveloperRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static ua.epam.springapp.utils.DeveloperGenerator.generateDevelopers;
import static ua.epam.springapp.utils.DeveloperGenerator.generateSingleDeveloper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class DeveloperServiceImplTest {

    private final DeveloperRepository developerRepository = Mockito.mock(DeveloperRepository.class);

    private final DeveloperMapper developerMapper = new DeveloperMapper(
            new AccountMapper(),
            new SkillMapper()
    );

    private final DeveloperServiceImpl sut = new DeveloperServiceImpl(developerRepository, developerMapper);

    @Test
    public void shouldGetAllDevelopers() {
        int developersSize = 10;
        List<Developer> developers = generateDevelopers(developersSize);
        List<DeveloperDto> developerDtoList = developers
                .stream()
                .map(developerMapper::toDto)
                .collect(Collectors.toList());

        doReturn(developers).when(developerRepository).getAll();

        List<DeveloperDto> foundDevelopers = sut.getAll();

        verify(developerRepository).getAll();
        assertThat(foundDevelopers, hasSize(developersSize));

        developerDtoList.forEach(
                developerDto -> assertThat(foundDevelopers, hasItem(allOf(
                        hasProperty("id", is(developerDto.getId())),
                        hasProperty("name", is(developerDto.getName())),
                        hasProperty("skills", is(developerDto.getSkills())),
                        hasProperty("account", is(developerDto.getAccount()))))
                )
        );
    }

    @Test
    public void shouldThrowWhenDevelopersListIsEmpty() {
        doReturn(new ArrayList<Developer>()).when(developerRepository).getAll();

        try {
            sut.getAll();
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(developerRepository).getAll();
    }

    @Test
    public void shouldGetDeveloper() {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();
        doReturn(developer).when(developerRepository).get(id);

        DeveloperDto foundDeveloperDto = sut.get(id);

        verify(developerRepository).get(id);

        assertThat(foundDeveloperDto, allOf(
                hasProperty("id", is(developerDto.getId())),
                hasProperty("name", is(developerDto.getName())),
                hasProperty("skills", is(developerDto.getSkills())),
                hasProperty("account", is(developerDto.getAccount())))
        );
    }

    @Test
    public void shouldThrowWhenDeveloperNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(developerRepository).get(anyId);

        try {
            sut.get(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(developerRepository).get(anyId);
    }

    @Test
    public void shouldAddDeveloper() {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);

        sut.add(developerDto);

        verify(developerRepository).add(developer);
    }

    @Test
    public void shouldUpdateDeveloper() {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();

        doReturn(developer).when(developerRepository).get(id);

        boolean updated = sut.update(id, developerDto);

        verify(developerRepository).get(id);

        assertThat(updated, is(true));
    }

    @Test
    public void shouldThrowWhenUpdatedDeveloperNotFound() {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();

        doReturn(null).when(developerRepository).get(id);

        try {
            sut.update(id, developerDto);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(developerRepository).get(id);
    }

    @Test
    public void shouldDeleteAccount() {
        Developer developer = generateSingleDeveloper();
        DeveloperDto developerDto = developerMapper.toDto(developer);
        UUID id = developer.getId();

        doReturn(developer).when(developerRepository).remove(id);

        DeveloperDto deletedDeveloper = sut.remove(id);

        verify(developerRepository).remove(id);

        assertThat(deletedDeveloper, allOf(
                hasProperty("id", is(developerDto.getId())),
                hasProperty("name", is(developerDto.getName())),
                hasProperty("skills", is(developerDto.getSkills())),
                hasProperty("account", is(developerDto.getAccount())))
        );
    }

    @Test
    public void shouldThrowWhenDeletedDeveloperNotFound() {
        UUID anyId = UUID.randomUUID();
        doReturn(null).when(developerRepository).get(anyId);

        try {
            sut.remove(anyId);
        } catch (Exception e) {
            assertThat(e, instanceOf(EntityNotFoundException.class));
        }

        verify(developerRepository).remove(anyId);
    }
}
