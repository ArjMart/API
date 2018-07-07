package com.arjvik.arjmart.api.jms;

public class PipelineException extends Exception {

	private static final long serialVersionUID = 1L;

	public PipelineException() {

	}

	public PipelineException(String message) {
		super(message);
	}

	public PipelineException(Throwable cause) {
		super(cause);
	}

	public PipelineException(String message, Throwable cause) {
		super(message, cause);
	}

	public PipelineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
