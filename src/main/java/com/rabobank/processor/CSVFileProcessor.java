package com.rabobank.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.BSLineItem;
import com.rabobank.model.ProcessInfo;
import com.rabobank.model.RecordStatus;


@Component
public class CSVFileProcessor implements FileProcessor {

	ProcessInfo records=null;
	public CSVFileProcessor() {
		records=new ProcessInfo();
	}	
	
	
	public ProcessInfo process(String file)throws RabobankAppException{
		
		BufferedReader br=null;
		
		try {
			br = new BufferedReader(new FileReader(new File(file)));
		
		String line=null;
		int count=0;
		double doubleValue=0;
		while((line=br.readLine())!=null) {
			System.out.println("processing csv");
			
			String record[]=line.split(",");
			count++;
			if(count==1) {
				continue;
			}
			if(record.length>=6) {
				BSLineItem obj=new BSLineItem();
				
				obj.setReferenceId(Long.parseLong(record[0]));
				obj.setIbanNo(record[1]);
				obj.setDescription(record[2]);
				doubleValue=record[3].trim().isEmpty()?0:Double.parseDouble(record[3]);
				obj.setStartBalance(doubleValue);
				doubleValue=record[4].trim().isEmpty()?0:Double.parseDouble(record[4]);
				obj.setMutation(doubleValue);
				doubleValue=record[5].trim().isEmpty()?0:Double.parseDouble(record[5]);
				obj.setEndBalance(doubleValue);
				
				double endBalance=obj.getStartBalance()+obj.getMutation();
				endBalance=new BigDecimal(endBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();
				boolean flag=false;
				if(endBalance!=obj.getEndBalance()) 
					obj.setRecordStatus(RecordStatus.INVALID_END_BALANCE);
				
				records.getRecords().add(obj);
			}
			
			
		}
		} catch (FileNotFoundException e) {
			throw new RabobankAppException(e);
		}
		 catch (IOException e) {
			 throw new RabobankAppException(e);
		}finally {
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return records;
	}

	

}
