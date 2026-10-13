
package com.project.github_repository_searcher.service;

import com.project.github_repository_searcher.dto.GithubRepositoriesDTO;
import com.project.github_repository_searcher.dto.GithubSearchResponse;
import com.project.github_repository_searcher.entity.GithubRepository;
import com.project.github_repository_searcher.repository.GithubDataRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GithubApiService githubApiService;

    @Mock
    private GithubDataRepository githubDataRepository;

    @InjectMocks
    private GithubRepositoryService githubRepositoryService;

    @Test
    void searchAndSaveRepositories_shouldReturnEmptyListWhenNoResults() {

        GithubSearchResponse response = new GithubSearchResponse();
        response.setItems(List.of());

        when(githubApiService.searchRepositries(
                "nonexistent-repository", null, "stars"))
                .thenReturn(response);

        List<?> result = githubRepositoryService
                .searchAndSaveRepositories(
                        "nonexistent-repository", null, "stars");

        assertTrue(result.isEmpty());

        verify(githubDataRepository, never()).save(
                org.mockito.ArgumentMatchers.any());
    }


    
    @Test
    void searchAndSaveRepositories_shouldSaveNewRepository() {

        GithubSearchResponse.RepositoryItem item =
                new GithubSearchResponse.RepositoryItem();

        item.setId(123L);
        item.setName("spring-boot-demo");
        item.setDescription("Demo repository");
        item.setLanguage("Java");
        item.setStars(500);
        item.setForks(100);
        item.setLastUpdated("2026-10-01T10:00:00Z");

        GithubSearchResponse.Owner owner =
                new GithubSearchResponse.Owner();
        owner.setLogin("developer123");
        item.setOwner(owner);

        GithubSearchResponse response = new GithubSearchResponse();
        response.setItems(List.of(item));

        when(githubApiService.searchRepositries(
                "spring-boot", "Java", "stars"))
                .thenReturn(response);

        when(githubDataRepository.findById(123L))
                .thenReturn(Optional.empty());

        when(githubDataRepository.save(any(GithubRepository.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(modelMapper.map(
                any(GithubRepository.class),
                eq(GithubRepositoriesDTO.class)))
                .thenAnswer(invocation -> {
                    GithubRepository repository = invocation.getArgument(0);
                    GithubRepositoriesDTO dto = new GithubRepositoriesDTO();
                    dto.setGithubId(repository.getGithubId());
                    dto.setName(repository.getName());
                    return dto;
                });

        List<GithubRepositoriesDTO> result =
                githubRepositoryService.searchAndSaveRepositories(
                        "spring-boot", "Java", "stars");

        assertEquals(1, result.size());
        assertEquals(123L, result.get(0).getGithubId());
        assertEquals("spring-boot-demo", result.get(0).getName());

        verify(githubDataRepository, times(1))
                .save(any(GithubRepository.class));
    }


    
    @Test
    void searchAndSaveRepositories_shouldUpdateExistingRepository() {

        // Arrange: GitHub returns updated repository details
        GithubSearchResponse.RepositoryItem item =
                new GithubSearchResponse.RepositoryItem();

        item.setId(123L);
        item.setName("spring-boot-demo-updated");
        item.setDescription("Updated description");
        item.setLanguage("Java");
        item.setStars(800);
        item.setForks(150);
        item.setLastUpdated("2026-10-10T10:00:00Z");

        GithubSearchResponse.Owner owner =
                new GithubSearchResponse.Owner();
        owner.setLogin("developer123");
        item.setOwner(owner);

        GithubSearchResponse response = new GithubSearchResponse();
        response.setItems(List.of(item));

        GithubRepository existing = new GithubRepository();
        existing.setGithubId(123L);
        existing.setName("spring-boot-demo");
        existing.setStars(500);

        when(githubApiService.searchRepositries(
                "spring-boot", "Java", "stars"))
                .thenReturn(response);

        when(githubDataRepository.findById(123L))
                .thenReturn(Optional.of(existing));

        when(githubDataRepository.save(any(GithubRepository.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(modelMapper.map(
                any(GithubRepository.class),
                eq(GithubRepositoriesDTO.class)))
                .thenAnswer(invocation -> {
                    GithubRepository repository = invocation.getArgument(0);

                    GithubRepositoriesDTO dto = new GithubRepositoriesDTO();
                    dto.setGithubId(repository.getGithubId());
                    dto.setName(repository.getName());
                    dto.setStars(repository.getStars());
                    return dto;
                });

        List<GithubRepositoriesDTO> result =
                githubRepositoryService.searchAndSaveRepositories(
                        "spring-boot", "Java", "stars");

        // Assert
        assertEquals(1, result.size());
        assertEquals("spring-boot-demo-updated", result.get(0).getName());
        assertEquals(800, result.get(0).getStars());

        verify(githubDataRepository, times(1))
                .findById(123L);

        verify(githubDataRepository, times(1))
                .save(any(GithubRepository.class));
    }


    
    @Test
    void getSavedRepositories_shouldFilterAndSortRepositories() {

        // Arrange
        GithubRepository repository = new GithubRepository();
        repository.setGithubId(123L);
        repository.setName("spring-boot-demo");
        repository.setLanguage("Java");
        repository.setStars(500);
        repository.setForks(100);

        when(githubDataRepository.findAll(
                org.mockito.ArgumentMatchers
                        .any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(repository));

        when(modelMapper.map(
                any(GithubRepository.class),
                eq(GithubRepositoriesDTO.class)))
                .thenAnswer(invocation -> {
                    GithubRepository entity = invocation.getArgument(0);

                    GithubRepositoriesDTO dto = new GithubRepositoriesDTO();
                    dto.setGithubId(entity.getGithubId());
                    dto.setName(entity.getName());
                    dto.setLanguage(entity.getLanguage());
                    dto.setStars(entity.getStars());
                    return dto;
                });

        // Act
        List<GithubRepositoriesDTO> result =
                githubRepositoryService.getSavedRepositories(
                        "Java", 100, "stars");

        // Assert
        assertEquals(1, result.size());
        assertEquals("spring-boot-demo", result.get(0).getName());
        assertEquals(500, result.get(0).getStars());

        verify(githubDataRepository, times(1)).findAll(
                org.mockito.ArgumentMatchers
                        .any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class));
    }


}
