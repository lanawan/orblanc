/**
 * Классический плоский объект - модель для хранения информации о посещаемости
 * 
 * @author Степан Мельничук
 */
package com.unetis.orblanc.model;

import java.io.Serializable;

public class Traffic implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String objId;
	private String theDate;
	private String sensor;
	private int rin;
	private int rout;

	/**
	 * Конструктор класса с полями
	 * 
	 * @param id		id
	 * @param objId		id этого объекта должен быть такой же, что и в константах objId
	 * @param theDate	дата или дата и время с точностью до часа
	 * @param sensor	имя или id входа
	 * @param rin		число вошедших
	 * @param rout		число вышедших
	 */
	public Traffic(int id, String objId, String theDate, String sensor, int rin, int rout) {
		super();
		this.id = id;
		this.objId = objId;
		this.theDate = theDate;
		this.sensor = sensor;
		this.rin = rin;
		this.rout = rout;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public int getRin() {
		return rin;
	}

	public void setRin(int rin) {
		this.rin = rin;
	}

	public int getRout() {
		return rout;
	}

	public void setRout(int rout) {
		this.rout = rout;
	}

}
