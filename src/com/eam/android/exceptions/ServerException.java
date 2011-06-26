package com.eam.android.exceptions;

public class ServerException extends Exception {

	private String errMessage;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerException(String msg) {
		super();
		errMessage = msg;
	}
	
	public String getErrMessage() {
		return errMessage;
	}
	
}
