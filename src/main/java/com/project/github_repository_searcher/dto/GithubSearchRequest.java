package com.project.github_repository_searcher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubSearchRequest {
    
    @NotBlank(message = "Search query must not be empty")
    private String query;
    private String language;
    private String sort;
}
