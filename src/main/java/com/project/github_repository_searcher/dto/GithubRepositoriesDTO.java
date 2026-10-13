package com.project.github_repository_searcher.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class GithubRepositoriesDTO {
    
    private Long githubId;
    private String name;
    private String description;
    private String owner;
    private String language;
    private int stars;
    private int forks;
    private Instant lastUpdated;
}
