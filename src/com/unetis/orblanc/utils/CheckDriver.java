/**
 * 
 * Класс проверки актуальности драйвера
 * обновляет консультируемый извне индикатор актуальности драйвера
 * 
 * @author Степан Мельничук
 */
package com.unetis.orblanc.utils;

import java.io.File;
import com.unetis.orblanc.utils.IO;

import com.unetis.orblanc.model.ObjectConfig;

public class CheckDriver {
	public boolean needDownload = false;	// Индикатор актуальности JDBC драйвера : нужно его скачать с сервера = true, а если не нужно = false
	
	/**
	 * Проверяет наличие и соответствие сигнатур серверной и локальной JDBC драйвера
	 * при отсутствии драйвера или неравенстве сигнатур индикатор актуальности needDownload становится = true
	 * 
	 * @param objectConfig		Объект с конфигурацией этой программы
	 * @throws MyException
	 */
	public CheckDriver(ObjectConfig objectConfig) throws MyException{
		try{
			// Проверка наличия драйвера
			String filename = objectConfig.getDriverFilename();
			File file = new File(filename);
			if(!file.exists()){
				needDownload = true;
				return;
			}
			// Сверка сигнатур SHA1
			if(!IO.verifyChecksum(filename,objectConfig.getDriverSha1())){
				needDownload = true;
				return;
			}
		}
		catch(Exception e){
			throw new MyException(e);
		}
	}
}
