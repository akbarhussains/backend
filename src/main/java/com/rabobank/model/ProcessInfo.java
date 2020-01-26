package com.rabobank.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessInfo {

	List<BSLineItem> records=new ArrayList<BSLineItem>();
	String processedFile;
	public List<BSLineItem> getRecords() {
		return records;
	}
	public void setRecords(List<BSLineItem> records) {
		this.records = records;
	}
	public String getProcessedFile() {
		return processedFile;
	}
	public void setProcessedFile(String processedFile) {
		this.processedFile = processedFile;
	}
	
	
	
	
	
	
}
