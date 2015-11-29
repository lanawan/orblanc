package com.unetis.orblanc.application;
/**
 * Необходимые константы
 * 
 * @author Степан Мельничук
 *
 */
public class Constants {
	public static final String programVersion="Version 1.0 (2016-01-01) : ";
	public static final String objId="34"; // Уникальный id клиента, который ему назначен в админке и прописан в базу данных на сервере
	public static final boolean traffic=true; // Клиент для проверки посещаемости = true, клиент для проверки выручки = false
	public static final int programTimeout=40000; // Таймаут программы, чтобы случайно не зависала. Пока не используется
	public static final String remoteService="http://localhost:8080/diamant/";
}
