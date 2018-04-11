package com.kong.sboot.aSpringBootDemo.events;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kong.sboot.aSpringBootDemo.github.GithubClient;
import com.kong.sboot.aSpringBootDemo.github.RepositoryEvent;

//localhost:8080/events/spring-boost
@Controller
public class EventsController {

	private final GithubProjectRepository repository;
	private final GithubClient client;

	public EventsController(GithubProjectRepository repository, GithubClient client) {
		super();
		this.repository = repository;
		this.client = client;
	}

	@GetMapping("events/{repoName}")
	@ResponseBody
	public RepositoryEvent[] fetchEvents(@PathVariable String repoName) {
		// add part with GithubProject project = to secure that this.repository is
		// GithubProject
		GithubProject project = this.repository.findByRepoName(repoName);

		return this.client.fetchEvents(project.getOrgName(), project.getRepoName()).getBody();
	}

	@GetMapping("/")
	public String dashboard(Model model) {
		List<DashboardEntry> entries = StreamSupport
				.stream(this.repository.findAll().spliterator(), true)
				.map(p -> new DashboardEntry(p, client.fetchEventsList(p.getOrgName(), p.getRepoName())))
				.collect(Collectors.toList());
		model.addAttribute("entries", entries);
		return "dashboard";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("projects", repository.findAll());
		return "admin";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
		}
	

	@RequestMapping(value="/username", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserPrincipal(Principal principal) {
		return principal.getName();
	}	
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserAuthentication(Authentication authentication){
		return authentication.getName();
	}

}
