package com.rabobank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.rabobank.process.FileManager;

@SpringBootApplication
@ComponentScan(basePackages="com.rabobank.*")
/**
 * Entry point of application
 * @author Jaheen Afsar
 *
 */
public class BsLineItemExtractorApplication {

	
	
	
	public static void main(String[] args) {
		
		String files[]=null;
		boolean flag=false;
		for(String val:args) {
			if(val.startsWith("--csFiles")) {
				files=val.substring(val.indexOf("=")+1).split(",");
				flag=true;
				break;
			}
		}
		
		if(flag) {
		ApplicationContext context=SpringApplication.run(BsLineItemExtractorApplication.class, args);
		
		FileManager fileManager=context.getBean(FileManager.class);
		if(files!=null)
			fileManager.extract(files);
		}
		else {
			System.out.println("Please provide files to scan as --csFiles=records.csv,records.xml in program argument !!!");
		}
	}

}
