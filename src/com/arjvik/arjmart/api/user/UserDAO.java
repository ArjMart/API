package com.arjvik.arjmart.api.user;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.order.Order;

public interface UserDAO {

	public User getUser(int ID) throws UserNotFoundException, DatabaseException;

	public User getUserByUUID(int UUID) throws UserNotFoundException, DatabaseException;

	public int addUser(User user) throws UserAlreadyExistsException, DatabaseException;

	public void editUserCreditCardNumber(int ID, User user) throws UserNotFoundException, DatabaseException;

	public boolean authenticate(User user) throws DatabaseException;
	
	public void deleteUser(int ID) throws UserNotFoundException, DatabaseException;

	public List<Order> getUserOrders(int ID) throws UserNotFoundException, DatabaseException;

}
