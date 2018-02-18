package com.arjvik.arjmart.api.user;

import com.arjvik.arjmart.api.DatabaseException;

public interface UserDAO {

	public User getUser(int ID) throws UserNotFoundException, DatabaseException;

	public int addUser(User user) throws UserAlreadyExistsException, DatabaseException;

	public void editUserCreditCardNumber(int ID, User user)  throws UserAlreadyExistsException, DatabaseException;

	public boolean authenticate(User user) throws DatabaseException;

}
