package com.azienda.esempioRest3.exception;

public class LocalitaNotExistsException extends Exception {

	public LocalitaNotExistsException(String message, Throwable cause) {
		super("La località non esiste!", cause);
		
	}
	
}
