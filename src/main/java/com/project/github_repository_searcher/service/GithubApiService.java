package com.project.github_repository_searcher.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.project.github_repository_searcher.dto.GithubSearchResponse;

@Service 
public class GithubApiService {
    private final RestClient restClient;

    public GithubApiService(RestClient.Builder resClientBuilder){
        this.restClient=resClientBuilder.baseUrl("https://api.github.com").build();
    }
    

    public GithubSearchResponse searchRepositries(String query, String language, String sort){
        final String searchQuery= query + " in:name" + ((language != null && !language.isBlank()) ? " language:" + language : "");

        String sortBy = switch(sort == null ? "stars": sort.toLowerCase()){
            case "forks" -> "forks";
            case "updated" -> "updated";
            default -> "stars";
        };

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/search/repositories")
                    .queryParam("q", searchQuery)
                    .queryParam("sort", sortBy)
                    .build())
                .retrieve().body(GithubSearchResponse.class);
    }

}
