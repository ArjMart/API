package com.arjvik.arjmart.api.order;

import java.util.List;

import com.arjvik.arjmart.api.DatabaseException;

public interface OrderLineDAO {

	public List<OrderLine> getOrderLines(int orderID) throws DatabaseException;

	public OrderLine getOrderLine(int orderID, int orderLineID) throws OrderLineNotFoundException, DatabaseException;

	public int addOrderLine(OrderLine orderLine) throws OrderLineCombinationAlreadyExistsException, DatabaseException;

	public void updateOrderLineStatus(int orderID, int orderLineID, Status status) throws OrderLineNotFoundException, DatabaseException;

	public void updateOrderLineQuantity(int orderID, int orderLineID, Quantity quantity) throws OrderLineNotFoundException, DatabaseException;

	public void deleteOrderLine(int orderID, int orderLineID) throws OrderLineNotFoundException, DatabaseException;

}
