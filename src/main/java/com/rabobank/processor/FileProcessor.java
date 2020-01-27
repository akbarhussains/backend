package com.rabobank.processor;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.ProcessInfo;

/**
 *  Base Interface for processing the files
 * @author Jaheen Afsar
 *
 */
public interface FileProcessor {

	/**
	 * To extract the records from the given file
	 * @param file
	 * @return
	 * @throws RabobankAppException
	 */
	public ProcessInfo process(String file)throws RabobankAppException;
}
