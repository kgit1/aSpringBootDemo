package com.kong.sboot.aSpringBootDemo.events;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GithubProjectRepository extends PagingAndSortingRepository<GithubProject, Long> {

	GithubProject findByRepoName(String repoName);
}
