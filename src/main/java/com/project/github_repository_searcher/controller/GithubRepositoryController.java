package com.project.github_repository_searcher.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.github_repository_searcher.dto.GithubRepositoriesDTO;
import com.project.github_repository_searcher.dto.GithubSearchRequest;
import com.project.github_repository_searcher.service.GithubRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(
    name = "GitHub Repository Searcher",
    description = "Search GitHub repositories and retrieve saved repository data"
)
@RestController 
@RequestMapping("/api/github")
@AllArgsConstructor 
public class GithubRepositoryController {
    private final GithubRepositoryService githubRepositoryService;


    @Operation(
        summary = "Search and save GitHub repositories"
    )
    @PostMapping("/search")
    public List<GithubRepositoriesDTO> searchRepositories(@Valid @RequestBody GithubSearchRequest request){
        return githubRepositoryService.searchAndSaveRepositories(request.getQuery(), request.getLanguage(), request.getSort());
    }


    @Operation(
        summary = "Retrieve saved repositories"
    )
    @GetMapping("/repositories")
    public List<GithubRepositoriesDTO> getSavedRepositories(@RequestParam(required = false) String language, @RequestParam(required = false) Integer minStars, @RequestParam(required = false, defaultValue = "stars") String sort){

        return githubRepositoryService.getSavedRepositories(language, minStars, sort);
    }
}
