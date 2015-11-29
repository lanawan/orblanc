/**
 * Приложение "orblanc" - программа клиент
 * Используется для передачи данных о выручке или посещаемости торгового центра с клиентского компьютера на сервер
 * Серверное приложение должно быть "diamant"  
 * Источник данных : любая SQL база
 * Протокол передачи : http
 * Архитектура передачи : REST
 * Шифрование : AES 256
 * JDBC Драйвер к базе-источнику загружается с сервера только при надобности
 * Ввиду того, что жесткое требование к клиенту - это его легкий вес, то никакой фреймворк и никакая сторонняя библиотека
 * не используется. Исключение составляют лишь собственная библиотека шифрования и простейшая библиотека работы с JSON
 * В константах каждого клиента прошит его уникальный id
 * Все необходимые параметры для работы с базой данных клиент получает с сервера.
 * 
 * @author Степан Мельничук
 * @version 1.0
 * 
 */
package com.unetis.orblanc.application;

import com.unetis.orblanc.dao.RevenueDao;
import com.unetis.orblanc.dao.TrafficDao;
import com.unetis.orblanc.model.ObjectConfig;
import com.unetis.orblanc.model.Revenue;
import com.unetis.orblanc.model.Traffic;
import com.unetis.orblanc.net.Communicate;
import com.unetis.orblanc.utils.CheckDriver;
import com.unetis.orblanc.utils.DriverLoader;
import com.unetis.orblanc.utils.IO;
import com.unetis.orblanc.utils.MyException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * 
 * Основной класс приложения
 * Последовательность работы :
 * 1. Скачиваем с сервера конфигурацию для этого клиента
 * 2. Проверяем наличие локального JDBC драйвера и его актуальность. Если нужно, скачиваем с сервера JDBC драйвер для этого клиента
 * 3. Загружаем JDBC драйвер в память
 * 4. Коннектимся к базе, запускаем SQL-запрос и результат пишем в соответствующий класс (ORM)
 * 5. Созданный список классов, хранящих запрошенную информацию, отправляем на сервер
 * 
 * Все общение с сервером зашифровано. Шифрование осуществляется внешней библиотекой
 *
 */
public class Application {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws MyException {
		try{
			// Запросы get и post находятся в классе Communicate
			Communicate com = new Communicate();
			
			// get запросом получаем с сервера конфигурацию для этого клиента, используя адрес :
			// http://<server_host:server_port>/objectConfig/config/<objId> где objId - уникальный id этого клиента известный серверу
			String response = new String(com.get(new URL(Constants.remoteService+"objectConfig/config"+Constants.objId)),"UTF-8");

			// Используя JSON парсер, полученный объект преобразуем в объект (см. model), соответствующий конфигурации. 
			JSONObject jsonObjectConfig = (JSONObject)new JSONParser().parse(response);
			JSONObject jsonDrver = (JSONObject)jsonObjectConfig.get("driver");
			ObjectConfig objectConfig =  new ObjectConfig(
															(String)jsonObjectConfig.get("dbConnectionString"),
															(String)jsonObjectConfig.get("dbUser"),
															(String)jsonObjectConfig.get("dbPassword"),
															(String)jsonObjectConfig.get("sqlQuery"),
															(String)jsonDrver.get("driverFilename"),
															(String)jsonDrver.get("driverSha1"),
															(String)jsonDrver.get("driverClass"));
			
			// Проверяем наличие JDBC драйвера и его актуальность, сверяя его электронную сигнатуру с той, что на сервере
			CheckDriver checkDriver = new CheckDriver(objectConfig);
			// Если он отсутствует, или не актуален
			if(checkDriver.needDownload){
				// get запросом скачиваем JDBC драйвер, используя адрес :
				// http://<server_host:server_port>/objectConfig/driver/<objId> где objId - уникальный id этого клиента известный серверу
				IO.putBytesToFile(objectConfig.getDriverFilename(), com.get(new URL(Constants.remoteService+"objectConfig/driver"+Constants.objId)));
			}

			// Загружаем драйвер в память
			DriverLoader driverLoader = DriverLoader.getInstance();
			driverLoader.loadDriver(objectConfig);

			// Смотрим для чего сделан этот клиент : для получения выручки, либо для посещаемости
			// и в соответствии с этим запускаем поиск данных в базе
			// Найденные данные данные будут преобразованы в соответствующие объекты
			JSONArray jArray = new JSONArray();
			if(Constants.traffic){
				// Получен список объектов для посещаемости
				List<Traffic> lt = TrafficDao.getTraffic(objectConfig);
				// Преобразуем список в JSON array
				for(Traffic traffic : lt){
					JSONObject jobj = new JSONObject();
					jobj.put("objId", traffic.getObjId());
					jobj.put("rin", traffic.getRin());
					jobj.put("rout", traffic.getRout());
					jobj.put("sensor", traffic.getSensor());
					jobj.put("theDate", traffic.getTheDate());
					jArray.add(jobj);
				}
				// Отправляем полученные и преобразованные данные на сервер, используя адрес :
				// http://<server_host:server_port>/traffic/create<objId>
				com.post(new URL(Constants.remoteService+"traffic/create"+Constants.objId), jArray.toJSONString());
			}
			else{
				// Получен список объектов для выручки
				List<Revenue> lt = RevenueDao.getRevenue(objectConfig);
				// Преобразуем список в JSON array
				for(Revenue revenue : lt){
					JSONObject jobj = new JSONObject();
					jobj.put("objId", revenue.getObjId());
					jobj.put("pos", revenue.getPos());
					jobj.put("total", revenue.getTotal());
					jobj.put("realSum", revenue.getRealSum());
					jobj.put("theDate", revenue.getTheDate());
					jArray.add(jobj);
				}
				// Отправляем полученные и преобразованные данные на сервер, используя адрес :
				// http://<server_host:server_port>/revenue/create<objId>
				com.post(new URL(Constants.remoteService+"revenue/create"+Constants.objId), jArray.toJSONString());
			}
		// Перехват необходимых исключений в пользовательское исключение
		} catch(UnsupportedEncodingException e) {
			throw new MyException("Main error : ",e);
		} catch(MalformedURLException e) {
			throw new MyException("Main error : ",e);
		} catch(ParseException e) {
			throw new MyException("Main error : ",e);
		}
	}

}
