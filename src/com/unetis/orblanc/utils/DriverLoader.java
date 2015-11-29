/**
 * Класс синглтон для динамической загрузки JDBC драйвера в память
 * 
 * Это сделано с целью использовать один и тот же клиент (эту программу) с любой базой данных.
 * Так как это сервер содержит и передаёт клиенту информацию о базе данных.
 * В том числе и JDBC драйвер, который нужно будет загрузить в память.
 * 
 * @author Степан Мельничук
 */

package com.unetis.orblanc.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.unetis.orblanc.model.ObjectConfig;

public class DriverLoader {
	/**
	 * Lazy holder - это для создания экземпляра этого класса не в переменной, а в константе причем в статической для паттерна синглтон.
	 * 
	 * Синглтон здесь используется в качестве шаблона для того, чтобы драйвер был загружен только один раз 
	 * @author Степан Мельничук
	 *
	 */
	public static class LazyHolder {
		public static final DriverLoader INSTANCE = new DriverLoader();
	}

	public static DriverLoader getInstance() {
		return LazyHolder.INSTANCE;
	}
	/**
	 * Метод для динамической загрузки JDBC драйвера
	 * 
	 * этот метод использует рефлексию
	 * 
	 * @param objectConfig
	 * @throws MyException
	 */
	public void loadDriver(ObjectConfig objectConfig) throws MyException {
		// Явовский лоадер классов будет тоже константой для единственности исполнения
        final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        try {
        	// Сначала создаём инструмент, которым будем загручать с диска в память
        	// Просим лоадер классов загрузить нечто создав маппинг внутреннего метода "addURL" класса URL
        	// Теперь маппированный method может просто скопировать что-то с диска в память
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            // А теперь сказать смаппированному методу, что это нечто есть такой-то файл и разместить содержимое этого файла в памяти
            method.invoke(loader, new File(objectConfig.getDriverFilename()).toURI().toURL());
            // Разместить то разместили, но где в памяти именно он находится - пока не известно
            // Поэтому сейчас понадобится создать экземпляр запущенного в памяти драйвера. Для этого :
            // - перехватить загруженный в память класс, зная его имя из конфигурации
            // - создать экземпляр генерик этого класса
            Class<?> classToLoad = Class.forName(objectConfig.getDriverClass(), true, loader);
            // Генерик то есть, то есть мы знаем границы в памяти нашего драйвера. Но пока что между этими границами только набор байтов
            // Для его работы нужно вызвать конструктор загруженного класса. Иными словами, запустить этот набор байтов, чтоб он стал активным.
            // Запускаем нулевой конструктор нашего генерика
            classToLoad.newInstance();
		} catch(NoSuchMethodException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(SecurityException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(IllegalAccessException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(IllegalArgumentException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(InvocationTargetException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(MalformedURLException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(ClassNotFoundException e) {
			throw new MyException("loadDriver error : "+e);
		} catch(InstantiationException e) {
			throw new MyException("loadDriver error : "+e);
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}
