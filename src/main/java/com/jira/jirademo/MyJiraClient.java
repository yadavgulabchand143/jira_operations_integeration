package com.jira.jirademo;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

@Component
public class MyJiraClient {

    private String username;
    private String password;
    private String jiraUrl;
    JiraRestClient restClient;
    
    
    @Value("${jira.username}")
	private String jiraUserName;
	
	@Value("${jira.password}")
	private String jiraPassword;
	
	@Value("${jira.url}")
	private String jiraURL ;

	@PostConstruct
    public void init() {
        this.username = jiraUserName;
        this.password = jiraPassword;
        this.jiraUrl = jiraURL;
        this.restClient = getJiraRestClient();
    }

	@PreDestroy
	public void destroy() {
		try {
			this.restClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public String createIssue(String projectKey, Long issueType, String issueSummary) {
		System.out.println("BEGIN ::: Inside createIssue() method of MyJiraClient");
		IssueRestClient issueClient = null;
		IssueInput issue = null;
		try {
			issueClient = restClient.getIssueClient();

			// IssueInputBuilder newIssue = new IssueInputBuilder(projectKey, issueType,
			// issueSummary);

			IssueInputBuilder newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary);

			newIssue.setProjectKey(projectKey);
			newIssue.setSummary(issueSummary);
			newIssue.setIssueTypeId(issueType);
			newIssue.setDescription("Test Description #");
			newIssue.setPriorityId(4L);

			issue = newIssue.build();

		} catch (Exception e) {
			System.out.println("Exception occured while creating new issue in JIRA");
		}
		System.out.println("END ::: Inside createIssue() method of MyJiraClient");
		return issueClient.createIssue(issue).claim().getKey();
	}

    public Issue getIssue(String issueKey) {
    	System.out.println("BEGIN ::: Inside getIssue() method of MyJiraClient");
        return restClient.getIssueClient().getIssue(issueKey).claim();
        
    }

    public void voteForAnIssue(String issueKey) {
    	System.out.println("BEGIN ::: Inside voteForAnIssue() method of MyJiraClient");
    	Issue issue = restClient.getIssueClient().getIssue(issueKey).claim();
        restClient.getIssueClient().vote(issue.getVotesUri()).claim();
    }

    public int getTotalVotesCount(String issueKey) {
    	System.out.println("BEGIN ::: Inside getTotalVotesCount() method of MyJiraClient");
        BasicVotes votes = getIssue(issueKey).getVotes();
        return votes == null ? 0 : votes.getVotes();
    }

    public void addComment(String issueKey, String commentBody) {
    	System.out.println("BEGIN ::: Inside addComment() method of MyJiraClient");
    	Issue issue = restClient.getIssueClient().getIssue(issueKey).claim();
        restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
    }

    public List<Comment> getAllComments(String issueKey) {
    	System.out.println("BEGIN ::: Inside getAllComments() method of MyJiraClient");
        return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false)
          .collect(Collectors.toList());
    }

    public void updateIssueDescription(String issueKey, String newDescription) {
    	System.out.println("BEGIN ::: Inside updateIssueDescription() method of MyJiraClient");
        IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
        restClient.getIssueClient().updateIssue(issueKey, input).claim();
    }

    public void deleteIssue(String issueKey, boolean deleteSubtasks) {
    	System.out.println("BEGIN ::: Inside deleteIssue() method of MyJiraClient");
        restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
    }

    private JiraRestClient getJiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
          .createWithBasicHttpAuthentication(getJiraUri(), this.username, this.password);
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}