package com.arjvik.arjmart.api.order.checkout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;
import com.arjvik.arjmart.api.order.OrderNotFoundException;

public class JDBCCheckoutDAO implements CheckoutDAO {
	
	private ConnectionFactory connectionFactory;
	private PaymentDAO paymentDAO;
	
	@Inject
	public JDBCCheckoutDAO(ConnectionFactory connectionFactory, PaymentDAO paymentDAO) {
		this.connectionFactory = connectionFactory;
		this.paymentDAO = paymentDAO;
	}

	@Override
	public void checkout(int orderID) throws OrderNotFoundException, InvalidOrderStateException, PaymentException, DatabaseException{
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from Order where OrderID = ?");
			statement.setInt(1, orderID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new OrderNotFoundException(orderID);
			String status = resultSet.getString("OrderStatus");
			if(!"Cart".equals(status))
				throw new InvalidOrderStateException(orderID, status);
			int userID = resultSet.getInt("UserID");
			
			statement = connection.prepareStatement("select * from User where UserID = ?");
			statement.setInt(1, userID);
			resultSet = statement.executeQuery();
			resultSet.next();
			String cc = resultSet.getString("CreditCardNumber");
			
			statement = connection.prepareStatement("select sum(OrderLine.Quantity * ItemPrice.Price) from OrderLine inner join ItemPrice on OrderLine.SKU = ItemPrice.SKU and OrderLine.ItemAttributeID = ItemPrice.ItemAttributeID where OrderID = ?");
			statement.setInt(1, orderID);
			resultSet = statement.executeQuery();
			resultSet.next();
			double price = resultSet.getDouble(1);
			
			paymentDAO.pay(price, cc);
			
			statement = connection.prepareStatement("update Order set OrderStatus = 'Pending' where OrderID = ?");
			statement.setInt(1, orderID);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
