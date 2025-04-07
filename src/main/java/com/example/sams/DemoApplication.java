package com.example.sams;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static ApplicationContext context;

	public static void main(String[] args) {
		context = new SpringApplicationBuilder(DemoApplication.class)
				.run(args);

		System.out.println("Spring context initialized: " + (context != null));

		// Display available service beans
		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("Available beans:");
		for (String beanName : beanNames) {
			if (beanName.contains("Service")) {
				System.out.println("Service bean: " + beanName);
			}
		}

	}
}