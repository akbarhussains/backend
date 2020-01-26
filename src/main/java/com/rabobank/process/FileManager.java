package com.rabobank.process;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.BSLineItem;
import com.rabobank.model.ProcessInfo;
import com.rabobank.model.RecordStatus;
import com.rabobank.processor.CSVFileProcessor;
import com.rabobank.processor.FileProcessor;
import com.rabobank.processor.XMLFileProcessor;

@Component
public class FileManager {
	
	Map<String, FileProcessor> processorMap=null;
	Map<String, ProcessInfo> processResultMap=null;
	
	
	
	private static Logger logger=LoggerFactory.getLogger(FileManager.class);
	
	public FileManager() {
		processorMap=new HashMap<String, FileProcessor>();
		processorMap.put("xml", new XMLFileProcessor());
		processorMap.put("csv", new CSVFileProcessor());
		processResultMap=new HashMap<String, ProcessInfo>();
	}
	
	
	public FileProcessor getFileProcessor(String ext)throws RabobankAppException {
		FileProcessor fileProcessor=null;
		
		if(ext.toLowerCase().equals("xml"))
			fileProcessor=processorMap.get("xml");
		else if(ext.toLowerCase().equals("csv"))
			fileProcessor=processorMap.get("csv");
		else
			throw new RabobankAppException("Un-Supported File Format");
		
		return fileProcessor;
	}
	
	public void getRecords(String files[]) {
		
		FileProcessor fileProcessor=null;
		
		logger.info("Initiated...............................");
		
		for(String file:files) {
			ProcessInfo result=null;
			try {
			
				fileProcessor=getFileProcessor(file.substring(file.lastIndexOf(".")+1));
				result=fileProcessor.process(file);
			}
			catch(Exception e) {
				logger.error("Exception caught "+e);
			}
			
			processResultMap.put(file, result);
			
		}
		
		
		
		List<BSLineItem> allRecords=processResultMap.entrySet().stream()
				.filter(entry->entry.getValue()!=null)
				.flatMap(entry->entry.getValue().getRecords().stream())
				.collect(Collectors.toList());
	
		
		
		System.out.println("===================================================================");
		
	
		List<BSLineItem> endBalanceMismatch=allRecords.
				parallelStream().filter(item->item.getRecordStatus().equals(RecordStatus.INVALID_END_BALANCE)).collect(Collectors.toList());
		
		Map<Long, List<BSLineItem>> duplicateMap=allRecords.stream().collect
				(Collectors.groupingBy(BSLineItem::getReferenceId));
				
		List<BSLineItem> result=duplicateMap.entrySet().stream().filter(entry->
			entry.getValue().size()>1).flatMap(entry->entry.getValue().stream()).collect(Collectors.toList());
	
		logger.info("========================Finished !!!!!!!!!====================================");
		
		result.addAll(endBalanceMismatch);
		
		result.stream().sorted((item1,item2)->((Long)item1.getReferenceId()).compareTo(item2.getReferenceId())).forEach(item->{
			System.out.println(item.getReferenceId()+"  "+item.getDescription());
		});
		
	
		
	}

	
	
}
