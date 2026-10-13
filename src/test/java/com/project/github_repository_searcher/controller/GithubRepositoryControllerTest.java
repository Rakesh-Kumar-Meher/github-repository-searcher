
package com.project.github_repository_searcher.controller;

import com.project.github_repository_searcher.dto.GithubRepositoriesDTO;
import com.project.github_repository_searcher.dto.GithubSearchRequest;
import com.project.github_repository_searcher.service.GithubRepositoryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GithubRepositoryController.class)
class GithubRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private GithubRepositoryService githubRepositoryService;

    @Test
    void searchRepositories_shouldReturnOk() throws Exception {

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("react");
        request.setLanguage("JavaScript");
        request.setSort("stars");

        GithubRepositoriesDTO dto = new GithubRepositoriesDTO();
        dto.setGithubId(123L);
        dto.setName("react-example");
        dto.setLanguage("JavaScript");
        dto.setStars(500);

        when(githubRepositoryService.searchAndSaveRepositories(
                "react", "JavaScript", "stars"))
                .thenReturn(List.of(dto));

        mockMvc.perform(post("/api/github/search")
                        .contentType("application/json")
                        .content("""
                                    {
                                    "query": "react",
                                    "language": "JavaScript",
                                    "sort": "stars"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("react-example"))
                .andExpect(jsonPath("$[0].stars").value(500));

        verify(githubRepositoryService).searchAndSaveRepositories(
                "react", "JavaScript", "stars");
    }

    
    @Test
    void getSavedRepositories_shouldReturnFilteredRepositories() throws Exception {

        GithubRepositoriesDTO dto = new GithubRepositoriesDTO();
        dto.setGithubId(123L);
        dto.setName("react-example");
        dto.setLanguage("JavaScript");
        dto.setStars(500);

        when(githubRepositoryService.getSavedRepositories(
                "JavaScript", 100, "stars"))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                org.springframework.test.web.servlet.request
                        .MockMvcRequestBuilders.get("/api/github/repositories")
                        .param("language", "JavaScript")
                        .param("minStars", "100")
                        .param("sort", "stars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("react-example"))
                .andExpect(jsonPath("$[0].language").value("JavaScript"))
                .andExpect(jsonPath("$[0].stars").value(500));

        verify(githubRepositoryService).getSavedRepositories(
                "JavaScript", 100, "stars");
    }

}
