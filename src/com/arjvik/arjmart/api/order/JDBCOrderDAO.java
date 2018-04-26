package com.arjvik.arjmart.api.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.user.UserNotFoundException;

public class JDBCOrderDAO implements OrderDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCOrderDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public List<Order> getAllOrders() throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from arjmart.Order");
			ResultSet resultSet = statement.executeQuery();
			List<Order> orders = new ArrayList<>();
			while(resultSet.next()){
				orders.add(new Order (resultSet.getInt("OrderID"), resultSet.getInt("UserID"), resultSet.getString("OrderStatus")));
			}
			return orders;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public List<Order> getAllOrdersWithStatus(String status) throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from arjmart.Order where OrderStatus = ?");
			statement.setString(1, status);
			ResultSet resultSet = statement.executeQuery();
			List<Order> orders = new ArrayList<>();
			while(resultSet.next()){
				orders.add(new Order (resultSet.getInt("OrderID"), resultSet.getInt("UserID"), resultSet.getString("OrderStatus")));
			}
			return orders;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public Order getOrder(int ID) throws OrderNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from arjmart.Order where OrderID=?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new OrderNotFoundException(ID);
			return new Order (ID, resultSet.getInt("UserID"), resultSet.getString("OrderStatus"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	@Override
	public OrderTotal getOrderTotal(int ID) throws OrderNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select sum(OrderLine.Quantity * ItemPrice.Price) from OrderLine inner join ItemPrice on OrderLine.SKU = ItemPrice.SKU and OrderLine.ItemAttributeID = ItemPrice.ItemAttributeID where OrderID = ?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new OrderNotFoundException(ID);
			double price = resultSet.getDouble(1);
			return new OrderTotal(price);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}	
	}
	
		@Override
		public Order getOrAddOrder(int userID) throws UserNotFoundException, DatabaseException {
			try (Connection connection = connectionFactory.getConnection()) {
				Order order = new Order();
				order.setUserID(userID);
				PreparedStatement statement = connection.prepareStatement("select * from arjmart.Order where UserID = ? and OrderStatus = 'Cart' order by OrderID desc limit 1");
				statement.setInt(1, userID);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()){
				order.setOrderID(resultSet.getInt("OrderID"));
				order.setStatus("Cart");
				return order;
			}
			//No cart order found, creating new one
			statement = connection.prepareStatement("insert into arjmart.Order (UserID) values (?)", Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, userID);
			statement.executeUpdate();
			resultSet = statement.getGeneratedKeys();
			if(!resultSet.next())
				throw new DatabaseException();
			order.setOrderID(resultSet.getInt(1));
			order.setStatus("Cart");
			return order;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new UserNotFoundException(userID, e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void updateOrderStatus(int ID, Status orderStatus) throws OrderNotFoundException, DatabaseException{
		getOrder(ID);
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("update arjmart.Order set OrderStatus = ? where OrderID = ?");
			statement.setString(1, orderStatus.getStatus());
			statement.setInt(2, ID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteOrder(int ID) throws OrderNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("delete from arjmart.Order where OrderID = ?");
			statement.setInt(1, ID);
			if(statement.executeUpdate()==0)
				throw new OrderNotFoundException(ID);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
}
