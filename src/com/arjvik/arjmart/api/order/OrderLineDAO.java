package com.arjvik.arjmart.api.order;

import java.util.List;

public interface OrderLineDAO {

	public List<OrderLine> getOrderLines(int orderID);

	public OrderLine getOrderLine(int orderID, int orderLineID);

	public int addOrderLine(OrderLine orderLine);

	public int updateOrderLine(int orderID, int orderLineID, OrderLine orderLine);

	public void deleteOrderLine(int orderID, int orderLineID);

}
