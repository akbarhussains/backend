package com.rabobank.processor;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.BSLineItem;
import com.rabobank.model.ProcessInfo;
import com.rabobank.model.RecordStatus;

@Component
/**
 * Implementation class for file processor to handle xml
 * @author Jaheen Afsar
 *
 */
public class XMLFileProcessor extends DefaultHandler implements FileProcessor{
	
	String qName=null;
	BSLineItem lineItem=null;
	ProcessInfo records=null;
	private static Logger logger=LoggerFactory.getLogger(XMLFileProcessor.class);

	public XMLFileProcessor() {
		records=new ProcessInfo();
	}
	
 public void startElement(String uri, String localName, String qName, Attributes attributes) {
	  this.qName=qName;
	
	if(qName.equalsIgnoreCase("record")){
		String ref= attributes.getValue("reference");
		ref=ref.trim().length()>0?ref.trim():"0";
		lineItem=new BSLineItem();
		lineItem.setReferenceId(Long.parseLong(ref));
	}
	
 }
 
 public void characters(char[] ch, int start, int length) {
	 
	if(ch ==null || String.copyValueOf(ch, start, length).trim().isEmpty())
		return;
	 
	String value=String.copyValueOf(ch, start, length).trim();
	if(qName.equalsIgnoreCase("accountNumber")) {
		lineItem.setIbanNo(value);
	}
	else if(qName.equalsIgnoreCase("description")) {
	
		lineItem.setDescription(value);
		
	}
	else if(qName.equalsIgnoreCase("startBalance") ) {
		
		lineItem.setStartBalance(Double.parseDouble(value));
		
	}
	else if(qName.equalsIgnoreCase("endBalance") ) {
    	lineItem.setEndBalance(Double.parseDouble(value));
		
	}
	else if(qName.equalsIgnoreCase("mutation") ) {
		lineItem.setMutation(Double.parseDouble(value));
	}
 }

 public void endElement(String uri, String localName, String qName) {
	 
	if(qName.equalsIgnoreCase("record")) {
		if(lineItem.getReferenceId()!=0) {
			double endBalance=lineItem.getStartBalance()+lineItem.getMutation();
			endBalance=new BigDecimal(endBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();
			if(endBalance!=lineItem.getEndBalance()) 
				lineItem.setRecordStatus(RecordStatus.INVALID_END_BALANCE);
			
		    records.getRecords().add(lineItem);
	 
		}
	}
	
 }

 public ProcessInfo getRecords() {
	return this.records;
 }

 

public ProcessInfo process(String file)throws RabobankAppException {
	
	SAXParserFactory factory = SAXParserFactory.newInstance();
	
	try {
		
		logger.info(" starting the extraction from xml ");
		SAXParser saxParser = factory.newSAXParser();
		XMLFileProcessor handler=new XMLFileProcessor();
		saxParser.parse(file, handler);
		records=handler.getRecords();
		logger.info("Completed extracting the xml ");
	} catch (ParserConfigurationException e) {
		throw new RabobankAppException(e);
	} catch (SAXException e) {
		throw new RabobankAppException(e);
	} catch (IOException e) {
		throw new RabobankAppException(e);
	}
	return records;
 
}
}