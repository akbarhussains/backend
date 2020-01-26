package com.rabobank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.rabobank.process.FileManager;

@SpringBootApplication
@ComponentScan(basePackages="com.rabobank.*")
public class BsLineItemExtractorApplication {

	
	
	
	public static void main(String[] args) {
		
		String files[]=null;
		for(String val:args) {
			if(val.startsWith("--csFiles")) {
				files=val.substring(val.indexOf("=")+1).split(",");
				break;
			}
		}
		
		ApplicationContext context=SpringApplication.run(BsLineItemExtractorApplication.class, args);
		
		FileManager fileManager=context.getBean(FileManager.class);
		if(files!=null)
			fileManager.getRecords(files);
		
	}

}
