package com.jira.jirademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JirademoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(JirademoApplication.class, args);
		EntryPoint entryPoint = context.getBean(EntryPoint.class);
		entryPoint.createIssue("DEMO", 10004L, "BUG Created from JRJC by SPRING BOOT");
		
	}

}
