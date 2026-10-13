package com.project.github_repository_searcher.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_repositories")
@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
public class GithubRepository {
    
    @Id 
    @Column(name = "github_id")
    private Long githubId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String owner;

    @Column 
    private String language;
    
    @Column 
    private int stars;

    @Column 
    private int forks;

    @Column(nullable = false)
    private Instant lastUpdated;

}
