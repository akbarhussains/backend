package com.rabobank.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.BSLineItem;
import com.rabobank.model.ProcessInfo;
import com.rabobank.model.RecordStatus;


@Component
/**
 * Implementation class for extracting the records from  csv file
 * @author Jaheen Afsar
 *
 */
public class CSVFileProcessor implements FileProcessor {

	ProcessInfo records=null;
	private static final Logger logger=LoggerFactory.getLogger(CSVFileProcessor.class);
	
	
	
	public CSVFileProcessor() {
		records=new ProcessInfo();
	}	
	
	
	public ProcessInfo process(String file)throws RabobankAppException{
		
		BufferedReader br=null;
		logger.info("Initiating extracting of the data for "+file+" ...............");
		try {
			br = new BufferedReader(new FileReader(new File(file)));
		
		String line=null;
		int count=0;
		double doubleValue=0;
		while((line=br.readLine())!=null) {
			String record[]=line.split(",");
			count++;
			if(count==1) {
				continue;
			}
			if(record.length>=6) {
				BSLineItem obj=new BSLineItem();
				long reference=record[0].trim().isEmpty()?0:Long.parseLong(record[0].trim());
				if(reference==0)
					continue;
				obj.setReferenceId(reference);
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
		
		logger.info("Completed processing of "+file);
		return records;
	}

	

}
