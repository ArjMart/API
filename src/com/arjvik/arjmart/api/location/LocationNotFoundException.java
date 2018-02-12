package com.arjvik.arjmart.api.location;

public class LocationNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	private int ID;

	public LocationNotFoundException(int ID) {
		super(Integer.toString(ID));
		this.ID = ID;
	}

	public LocationNotFoundException(int ID, Throwable cause) {
		super(Integer.toString(ID), cause);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
}
