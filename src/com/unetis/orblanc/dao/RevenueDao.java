/**
 * Класс прямого доступа к данным по выручке
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
import com.unetis.orblanc.model.Revenue;
import com.unetis.orblanc.utils.MyException;

public class RevenueDao {
	/**
	 * Возвращает ответ на SQL-запрос в виде списка объектов (ORM)
	 * @param oc			Объект конфигурации полученный с сервера заблаговременно. В этом объекте, в частности, содержится сам SQL-запрос
	 * @return				Список объектов выручки, каждый с соответствующими данными
	 * @throws MyException	
	 * @throws SQLException 
	 */
	public static List<Revenue> getRevenue(ObjectConfig oc) throws MyException {
		// Инициализация пустого списка объектов выручки
		LinkedList<Revenue> lt = new LinkedList<Revenue>();
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
				// Нас интересуют только эти поля. Эти алиасы обязательно должны присутствовать в SQL-запросе
				// Детали смотреть в com.unetis.orblanc.model.Revenue
				lt.add(new Revenue(0, Constants.objId, rs.getString("THEDATE"), rs.getString("POS"), rs.getInt("TOTAL"), rs.getDouble("REALSUM")));
			}
	        rs.close();
	        st.close();
	        connection.close();
		}
		catch(SQLException e){
				throw new MyException("getRevenue error : "+e);
		}
		return lt;
	}
}	
