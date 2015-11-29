/**
 * Классический плоский объект - модель для хранения информации о выручке
 * 
 * @author Степан Мельничук
 */
package com.unetis.orblanc.model;

import java.io.Serializable;

public class Revenue implements Serializable {
	private static final long serialVersionUID = 3L;
	private int id;
	private String objId;
	private String theDate;
	private String pos;
	private Integer total;
	private Double realSum;
	
	/**
	 * Конструктор класса с полями
	 * 
	 * @param id		id
	 * @param objId		id этого объекта должен быть такой же, что и в константах objId
	 * @param theDate	дата выручки
	 * @param pos		кассовый терминал point-of-sale (POS)
	 * @param total		общее количество продаж
	 * @param realSum	выручка по чекам после вычета возвратов
	 */
	public Revenue(int id, String objId, String theDate, String pos, Integer total, Double realSum) {
		super();
		this.id = id;
		this.objId = objId;
		this.theDate = theDate;
		this.pos = pos;
		this.total = total;
		this.realSum = realSum;
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

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Double getRealSum() {
		return realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}
}
