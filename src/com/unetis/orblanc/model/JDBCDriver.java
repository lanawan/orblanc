/**
 * Классический плоский объект - модель для хранения информации о JDBC драйвере
 * 
 * @author Степан Мельничук
 */

package com.unetis.orblanc.model;

import java.io.Serializable;

public class JDBCDriver implements Serializable{
	private static final long serialVersionUID = 5L;
	private int jdbc_driver_id;		// Опционально : id драйвера
	private String driverName;		// Опционально : имя драйвера
	private String driverFilename;	// Необходимое имя файла драйвера без полного доступа (только имя, а не full path)
	private String driverSha1;		// Сигнатура файла драйвера на сервере
	private String driverClass;		// Имя класса драйвера для использования в classForName(driverClass)
	
	public int getId() {
		return jdbc_driver_id;
	}

	public void setId(int id) {
		this.jdbc_driver_id = id;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
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
}
