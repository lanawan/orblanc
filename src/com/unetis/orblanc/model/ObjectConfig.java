/**
 * Классический плоский объект - модель для хранения информации о конфигурации этой программы
 * 
 * @author Степан Мельничук
 */
package com.unetis.orblanc.model;

import java.io.Serializable;

import com.unetis.orblanc.utils.MyException;

public class ObjectConfig implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private String dbConnectionString;
	private String dbUser;
	private String dbPassword;
	private String sqlQuery;
	private String driverFilename;
	private String driverSha1;
	private String driverClass;

	/**
	 * Конструктор класса конфигурации с полями
	 * 
	 * @param dbConnectionString		параметр "строка соединения" для getConnection()
	 * @param dbUser					параметр "пользователь" для getConnection()
	 * @param dbPassword				параметр "пароль" для getConnection()
	 * @param sqlQuery					SQL-запрос в базу
	 * @param driverFilename			Необходимое имя файла драйвера без полного доступа (только имя, а не full path)
	 * @param driverSha1				Сигнатура файла драйвера на сервере
	 * @param driverClass				Имя класса драйвера для использования в classForName(driverClass)
	 * @throws MyException
	 */
	public ObjectConfig(String dbConnectionString, String dbUser, String dbPassword, String sqlQuery, String driverFilename, String driverSha1, String driverClass) throws MyException {
		if(dbConnectionString==null || dbUser==null || dbPassword==null || sqlQuery==null || driverFilename==null || driverSha1==null){
			throw new MyException("One of the parameters for ObjectConfig is null : "+dbConnectionString+ ", " +dbUser+ ", " +dbPassword+ ", " +sqlQuery+ ", " +driverFilename + ", " + driverSha1 + ", " + driverClass);
		}
		this.dbConnectionString = dbConnectionString;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.sqlQuery = sqlQuery;
		this.driverFilename = driverFilename;
		this.driverSha1 = driverSha1;
		this.driverClass = driverClass;
	}

	public String getDbConnectionString() {
		return dbConnectionString;
	}

	public void setDbConnectionString(String dbConnectionString) {
		this.dbConnectionString = dbConnectionString;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getDriverFilename() {
		return driverFilename;
	}

	public void setDriverFilename(String driverFilename) {
		this.driverFilename = driverFilename;
	}

	public String getDriverSha1() {
		return driverSha1;
	}

	public void setDriverSha1(String driverSha1) {
		this.driverSha1 = driverSha1;
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	@Override
	public String toString() {
		return "ObjectConfig [dbConnectionString=" + dbConnectionString + ", dbUser=" + dbUser + ", dbPassword="
				+ dbPassword + ", sqlQuery=" + sqlQuery + ", driverFilename=" + driverFilename + ", driverSha1="
				+ driverSha1 + ", driverClass=" + driverClass + "]";
	}


}
