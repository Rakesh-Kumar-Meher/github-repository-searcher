package com.project.github_repository_searcher.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.github_repository_searcher.dto.GithubRepositoriesDTO;
import com.project.github_repository_searcher.dto.GithubSearchResponse;
import com.project.github_repository_searcher.entity.GithubRepository;
import com.project.github_repository_searcher.repository.GithubDataRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Service
public class GithubRepositoryService {

    private final ModelMapper modelMapper;
    private final GithubApiService githubApiService;
    private final GithubDataRepository githubDataRepository;

    private GithubRepositoriesDTO mapToDTO(GithubRepository repository) {
        return modelMapper.map(repository, GithubRepositoriesDTO.class);
    }

    @Transactional 
    public List<GithubRepositoriesDTO> searchAndSaveRepositories(String query, String language, String sort){
        GithubSearchResponse response = githubApiService.searchRepositries(query, language, sort);

        List<GithubRepositoriesDTO> savedRepositories = new ArrayList<>();

        if(response.getItems() == null || response == null || response.getItems().isEmpty()){
            return savedRepositories;
        }

        for(GithubSearchResponse.RepositoryItem item: response.getItems()){
            GithubRepository repository = githubDataRepository.findById(item.getId()).orElseGet(GithubRepository::new);

            repository.setGithubId(item.getId());
            repository.setName(item.getName());
            repository.setDescription(item.getDescription());
            repository.setOwner(item.getOwner()!= null ? item.getOwner().getLogin() : null);
            repository.setLanguage(item.getLanguage());
            repository.setStars(item.getStars());
            repository.setForks(item.getForks());
            repository.setLastUpdated(Instant.parse(item.getLastUpdated()));

            GithubRepository saved = githubDataRepository.save(repository);
            savedRepositories.add(mapToDTO(saved));
        }
        return savedRepositories;
    }


    public List<GithubRepositoriesDTO> getSavedRepositories(String language, Integer minStars, String sort){
        List<Specification<GithubRepository>> specs = new ArrayList<>();

        if(language != null && !language.isBlank()){
            specs.add((root, query, cb) -> 
                cb.equal(cb.lower(root.get("language")), language.toLowerCase())
            );
        }

        if(minStars != null){
            specs.add((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("stars"), minStars)
            );
        }

        String sortField = switch (
                sort == null ? "stars" : sort.toLowerCase()) {
            case "forks" -> "forks";
            case "updated" -> "lastUpdated";
            default -> "stars";
        };
        Specification<GithubRepository> specification = Specification.allOf(specs);

        Sort sorting = Sort.by(Sort.Direction.DESC, sortField);

        return githubDataRepository.findAll(specification, sorting)
            .stream()
            .map(repository ->
                modelMapper.map(repository, GithubRepositoriesDTO.class))
            .toList();
    }
}
