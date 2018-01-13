package com.arjvik.arjmart.api.order;

import java.util.List;

public class JDBCOrderLineDAO implements OrderLineDAO {

	@Override
	public List<OrderLine> getOrderLines(int orderID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderLine getOrderLine(int orderID, int orderLineID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addOrderLine(OrderLine orderLine) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateOrderLine(int orderID, int orderLineID, OrderLine orderLine) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteOrderLine(int orderID, int orderLineID) {
		// TODO Auto-generated method stub
		
	}

}
