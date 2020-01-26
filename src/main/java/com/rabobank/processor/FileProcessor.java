package com.rabobank.processor;

import com.rabobank.exception.RabobankAppException;
import com.rabobank.model.ProcessInfo;

public interface FileProcessor {


	public ProcessInfo process(String file)throws RabobankAppException;
}
