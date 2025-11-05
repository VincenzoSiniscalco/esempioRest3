package com.azienda.esempioRest3.exception;

public class NotIntegerException extends Exception {

	public NotIntegerException(String message, Throwable cause) {
		super("Il campo inserito non è un numero!", cause);
	}
	
}
