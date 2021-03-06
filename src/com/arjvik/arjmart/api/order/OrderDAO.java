package com.arjvik.arjmart.api.order;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.user.UserNotFoundException;

public interface OrderDAO {

	public List<Order> getAllOrders() throws DatabaseException;

	public List<Order> getAllOrdersWithStatus(String status) throws DatabaseException;

	public Order getOrder(int ID) throws OrderNotFoundException, DatabaseException;
	
	public OrderTotal getOrderTotal(int ID) throws OrderNotFoundException, DatabaseException;

	public Order getOrAddOrder(int userID) throws UserNotFoundException, DatabaseException;

	public void updateOrderStatus(int ID, Status orderStatus) throws OrderNotFoundException, DatabaseException;

	public void deleteOrder(int ID) throws OrderNotFoundException, DatabaseException;

}
