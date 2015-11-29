package com.unetis.orblanc.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IO {
	/**
	 * Запись бинарных данных в файл на диск
	 * 
	 * @param filename - имя файла для записи
	 * @param data - бинарное содержимое
	 * @throws MyException
	 * @throws IOException 
	 */
	public static void putBytesToFile(String filename, byte[] data) throws MyException {
		try{
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(data);
			fos.close();
		} catch(IOException e){
			throw new MyException("putBytesToFile error : "+e);
		} catch (Exception e){
			throw new MyException(e);
		}
	}
	/**
	 * Чтение файла с диска
	 * 
	 * @param filename - имя файла для чтения
	 * @return бинарное содержимое прочитанного файла
	 * @throws MyException
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(String filename) throws MyException, IOException{
		try{
			File file = new File(filename);
			int size = (int)file.length();
			byte[] bytes = new byte[size];
			if (size > Integer.MAX_VALUE){
				throw new MyException("getBytesFromFile error : file "+filename+" is too large");
			}
			else{
				DataInputStream dis = new DataInputStream(new FileInputStream(file));
				int read = 0;
				int numRead = 0;
				while (read < bytes.length && (numRead=dis.read(bytes, read, bytes.length-read)) >= 0) {
					read = read + numRead;
				}
				dis.close();
				if (read < bytes.length) {
					throw new MyException("Could not completely read file "+filename);
				}
				
			}
			return bytes;
		} catch(IOException e){
			throw new MyException("getBytesFromFile error : "+e);
		} catch(Exception e){
			throw new MyException("getBytesFromFile error : "+e);
		}
	}
    /**
     * Проверка соответствия сигнатуры файла с сигнатурой в параметрах
     * @param Имя файла без полного доступа. Подразумевается, что драйвер загружен туда же, где находится эта программа
     * @param testChecksum SHA1 ожидаемая сигнатура файла 
     * @return true если SHA1 сигнатуры файла и та, что в параметрах совпадают
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean verifyChecksum(String file, String testChecksum) throws MyException {
    	try{
    		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    		FileInputStream fis = new FileInputStream(file);

    		byte[] data = new byte[1024];
    		int read = 0; 
    		while ((read = fis.read(data)) != -1) {
    			sha1.update(data, 0, read);
    		}
    	
    		fis.close();
    	
    		byte[] hashBytes = sha1.digest();

    		StringBuffer sb = new StringBuffer();
    		for (int i = 0; i < hashBytes.length; i++) {
    			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
    		}

    		String fileHash = sb.toString();
    	
    		return fileHash.equals(testChecksum);
    	} catch(NoSuchAlgorithmException e){
    		throw new MyException("verifyChecksum error : "+e);
    	} catch(IOException e){
    		throw new MyException("verifyChecksum error : "+e);
    	} catch(Exception e){
    		throw new MyException("verifyChecksum error : "+e);
    	}
    }
}
