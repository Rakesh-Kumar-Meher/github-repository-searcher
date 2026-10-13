package com.project.github_repository_searcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.github_repository_searcher.entity.GithubRepository;

public interface GithubDataRepository extends JpaRepository<GithubRepository, Long>, JpaSpecificationExecutor<GithubRepository>{
    
}
