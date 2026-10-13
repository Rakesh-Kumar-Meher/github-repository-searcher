package com.project.github_repository_searcher.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class GithubSearchResponse {
    
    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("incomplete_results")
    private boolean incompleteResults;

    private List<RepositoryItem> items;

    @Getter
    @Setter
    public static class RepositoryItem {

        private Long id;
        private String name;
        private String description;
        private Owner owner;
        private String language;

        @JsonProperty("stargazers_count")
        private int stars;

        @JsonProperty("forks_count")
        private int forks;

        @JsonProperty("updated_at")
        private String lastUpdated;
    }

    @Getter
    @Setter
    public static class Owner {
        private String login;
    }   
}
