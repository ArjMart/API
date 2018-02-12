package com.arjvik.arjmart.api.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;

public class JDBCOrderLineDAO implements OrderLineDAO {

	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCOrderLineDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	@Override
	public List<OrderLine> getOrderLines(int orderID) throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from OrderLine");
			ResultSet resultSet = statement.executeQuery();
			List<OrderLine> orders = new ArrayList<>();
			while(resultSet.next()){
				orders.add(new OrderLine(
						resultSet.getInt("OrderID"), resultSet.getInt("OrderLineID"),
						resultSet.getInt("SKU"), resultSet.getInt("ItemAttributeID"),
						resultSet.getInt("Quantity"), resultSet.getString("Status")));
			}
			return orders;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public OrderLine getOrderLine(int orderID, int orderLineID) throws OrderLineNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from OrderLine where OrderID = ? and OrderLineID = ?");
			statement.setInt(1, orderID);
			statement.setInt(2, orderLineID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new OrderLineNotFoundException(orderID, orderLineID);
			OrderLine orderLine = new OrderLine(
									orderID, orderLineID,
									resultSet.getInt("SKU"), resultSet.getInt("ItemAttributeID"),
									resultSet.getInt("Quantity"), resultSet.getString("Status"));
			
			return orderLine;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public int addOrderLine(OrderLine orderLine) throws OrderLineCombinationAlreadyExistsException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("insert into OrderLine (OrderID, SKU, ItemAttributeID, Quantity, Status) from OrderLine values (?, ?, ?, ?, ?)");
			statement.setInt(1, orderLine.getOrderID());
			statement.setInt(2, orderLine.getSKU());
			statement.setInt(3, orderLine.getItemAttributeID());
			statement.setInt(4, orderLine.getQuantity());
			statement.setString(5, orderLine.getStatus());
			statement.executeUpdate();
			statement = connection.prepareStatement("select OrderLineID from OrderLine where OrderID = ? and SKU = ? and ItemAttributeID = ?");
			statement.setInt(1, orderLine.getOrderID());
			statement.setInt(2, orderLine.getSKU());
			statement.setInt(3, orderLine.getItemAttributeID());
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				return resultSet.getInt("OrderLineID");
			}else{
				throw new DatabaseException();
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OrderLineCombinationAlreadyExistsException(orderLine.getOrderID(), orderLine.getSKU(), orderLine.getItemAttributeID(), e);
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateOrderLineStatus(int orderID, int orderLineID, Status status) throws OrderLineNotFoundException, DatabaseException {
		getOrderLine(orderID, orderLineID);
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("update OrderLine set Status = ? where OrderID = ? and OrderLineID = ?");
			statement.setString(1, status.getStatus());
			statement.setInt(2, orderID);
			statement.setInt(3, orderLineID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public void updateOrderLineQuantity(int orderID, int orderLineID, Quantity quantity) throws OrderLineNotFoundException, DatabaseException {
		getOrderLine(orderID, orderLineID);
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("update OrderLine set Quantity = ? where OrderID = ? and OrderLineID = ?");
			statement.setInt(1, quantity.getQuantity());
			statement.setInt(2, orderID);
			statement.setInt(3, orderLineID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteOrderLine(int orderID, int orderLineID) throws OrderLineNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("delete from OrderLine where OrderID = ? and OrderLineID = ?");
			statement.setInt(1, orderID);
			statement.setInt(2, orderLineID);
			if(!(statement.executeUpdate()>0))
				throw new OrderLineNotFoundException(orderID, orderLineID);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
