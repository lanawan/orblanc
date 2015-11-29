/**
 * Класс отправки и получения данных с сервера содержащий post и get запросы
 * 
 * @author Степан Мельничук
 */

package com.unetis.orblanc.net;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.unetis.emeraude.AesCrypt;
import com.unetis.emeraude.AesException;
import com.unetis.orblanc.application.Constants;
import com.unetis.orblanc.utils.MyException;

public class Communicate {
	// Размер блока чтения с сервера
	public static final int blockSize=10240;

	/**
	 * post					отправка данных на сервер
	 * 
	 * @param postUrl		полный адрес отправления
	 * @param data			данные обычной строкой
	 * @throws MyException
	 * @throws AesException 
	 * @throws IOException 
	 */
	public void post(URL postUrl, String data) throws MyException{
		try{
			// Вот здесь мы шифруем данные, я не буду распространяться о деталях шифрования по соображениям безопасности
			AesCrypt crypt = new AesCrypt(Constants.objId);
			String encryptedData = crypt.encrypt(data);
		
			// Открываем соединение с сервером
			HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
			conn.setDoOutput(true);
			// POST - запрос
			conn.setRequestMethod("POST");
			// Шифровка пойдёт строкой, а в случае без шифрования было бы : conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
			// Таймауты для медленных соединений
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			// Отправка данных
			OutputStream os = conn.getOutputStream();
			os.write(encryptedData.getBytes());
			os.flush();

			// Какой статус ответа сервера : нормальный ?
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				// Если нет, то перехват в пользовательское исключение
				throw new MyException("Communicate post failed. HTTP error code : " + conn.getResponseCode());
			}

			// Так как назначение всей этой программы - это отправка данных на сервер (подобно набору шрифта на клавишах),
			// то ответа от сервера не ждем. У сервера есть логи и администратор их посмотрит, если что.
			
			conn.disconnect();
		} catch(IOException e) {
			throw new MyException("Communicate post error : "+e);
		} catch(AesException e) {
			throw new MyException("Communicate post error : "+e);
		} catch(Exception e) {
			throw new MyException("Communicate post error : "+e);
		}
	}

	/**
	 * get					получение данных с сервера
	 * 
	 * @param url			полный адрес get-запроса
	 * @return				бинарные расшифрованные данные
	 * @throws MyException
	 * @throws IOException 
	 * @throws AesException 
	 */
	public byte[] get(URL url) throws MyException{
		try{
			// Открываем соединение с сервером
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Все, что зашифровано будет читаться строкой, а в случае без шифрования было бы : conn.setRequestProperty("Accept", "application/json"); 
			conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
			
			// Таймауты для медленных соединений
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			// Какой статус ответа сервера : нормальный ?
			if (conn.getResponseCode() != 200) {
				// Если нет, то перехват в пользовательское исключение
				throw new MyException("Communicate get failed. HTTP error code : "+ conn.getResponseCode());
			}
			// Открываем канал для чтения
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			// И конвертер из прочтенных int в цепочку byte 
			ByteArrayOutputStream out = new ByteArrayOutputStream(blockSize);
			int len = blockSize;
	    	byte[] buff = new byte[len];
	    	// Чтение из открытого канала
			int sz;
            while ((sz = in.read(buff, 0, len)) != -1) {
                out.write(buff, 0, sz);
            }
            // Перевод из последовательности байт в стринг 
            String encoded = new String(out.toByteArray());
            
            // Вот здесь мы расшифровываем, я не буду распространяться о деталях шифрования по соображениям безопасности
			AesCrypt crypt = new AesCrypt(Constants.objId);
			byte[] decoded = crypt.decrypt(encoded);

			// Проверка : не содержит ли ответ сервера сообщение об ошибке
			String response = new String(decoded);
			if(response.startsWith("Error:")){
				// Если содержит, то это не данные для дальнейшей обработки мы получили, а вылет в исключение
				throw new MyException(response);
			}
         
	        out.close(); //just for fun, has no effect
	        in.close();
	        
	        conn.disconnect();

	        // Здесь метод возвращает данные
	        return decoded;
		} catch(IOException e) {
			throw new MyException("Communicate get error : "+e);
		} catch(AesException e) {
			throw new MyException("Communicate get error : "+e);
		} catch(Exception e) {
			throw new MyException("Communicate get error : "+e);
		}

	}

}

