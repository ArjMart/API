package com.arjvik.arjmart.api.location;

public class LocationNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;
	
	public LocationNotFoundException() {
	}

	public LocationNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public LocationNotFoundException(Throwable cause) {
		super(cause);
	}

	public LocationNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public LocationNotFoundException(int ID, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(Integer.toString(ID), cause, enableSuppression, writableStackTrace);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
