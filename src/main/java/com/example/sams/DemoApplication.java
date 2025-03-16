package com.example.sams;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import frontend.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.sams", "frontend"})
public class DemoApplication {

	public static ApplicationContext context;

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

		// Create a custom SpringApplicationBuilder with headless mode disabled
		context = new SpringApplicationBuilder(DemoApplication.class)
				.headless(false)
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

		SwingUtilities.invokeLater(App::startApp);

	}
}