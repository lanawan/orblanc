/**
 * Класс прямого доступа к данным по посещаемости
 * 
 * @author Степан Мельничук
 * 
 */
package com.unetis.orblanc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.unetis.orblanc.application.Constants;
import com.unetis.orblanc.model.ObjectConfig;
import com.unetis.orblanc.model.Traffic;
import com.unetis.orblanc.utils.MyException;

public class TrafficDao {
	/**
	 * Возвращает ответ на SQL-запрос в виде списка объектов (ORM)
	 * @param oc			Объект конфигурации полученный ранее с сервера. В этом объекте, в частности, содержится сам SQL-запрос
	 * @return				Список объектов с данными по посещаемости
	 * @throws MyException	
	 * @throws SQLException 
	 */
	public static List<Traffic> getTraffic(ObjectConfig oc) throws MyException {
		// Инициализация пустого списка объектов выручки
		LinkedList<Traffic> lt = new LinkedList<Traffic>();
		Connection connection = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			// Подключение к базе данных
			connection = DriverManager.getConnection(oc.getDbConnectionString(),oc.getDbUser(),oc.getDbPassword());
			st = connection.createStatement();
			
			// Выполнение запроса в базу
			rs = st.executeQuery(oc.getSqlQuery());
			
			// Чтение ответа и наполнение им списка объектов выручки 
			while(rs.next()){
				lt.add(new Traffic(0, Constants.objId, rs.getString("THEDATE"), rs.getString("SENSOR"), rs.getInt("RIN"), rs.getInt("ROUT")));
			}
	        rs.close();
	        st.close();
	        connection.close();

		}
		catch(Exception e){
			throw new MyException("getTraffic error : "+e);
		}
		return lt;
	}
}
