package com.jira.jirademo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.domain.Comment;

@Component
public class EntryPoint {

	@Autowired
	MyJiraClient myJiraClient;

	/**
	 * @param projectKey
	 * @param issueType
	 * @param issueSummary
	 */
	public void createIssue(String projectKey, Long issueType, String issueSummary) {
		try {
			myJiraClient.createIssue(projectKey, issueType, issueSummary);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 */
	public void getIssue(String issueKey) {
		try {
			myJiraClient.getIssue(issueKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 * @param issueDescription
	 */
	public void updateIssueDescription(String issueKey, String issueDescription) {
		try {
			myJiraClient.updateIssueDescription(issueKey, issueDescription);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 */
	public void voteForAnIssue(String issueKey) {
		try {
			myJiraClient.voteForAnIssue(issueKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 * @param commentBody
	 */
	public void addComment(String issueKey, String commentBody) {
		try {
			myJiraClient.addComment(issueKey, commentBody);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 * @param deleteSubtasks
	 */
	public void deleteIssue(String issueKey, boolean deleteSubtasks) {
		try {
			myJiraClient.deleteIssue(issueKey, deleteSubtasks);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 */
	public void getAllComments(String issueKey) {
		try {
			List<Comment> comments = myJiraClient.getAllComments(issueKey);
			comments.forEach(c -> System.out.println(c.getBody()));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param issueKey
	 */
	public void getTotalVotesCount(String issueKey) {
		try {
			myJiraClient.getTotalVotesCount(issueKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
