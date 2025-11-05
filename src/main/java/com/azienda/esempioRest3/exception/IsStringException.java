package com.azienda.esempioRest3.exception;

public class IsStringException extends Exception {

	public IsStringException(String message, Throwable cause) {
		super("Non hai inserito una stringa valida!", cause);
	}
	
}
