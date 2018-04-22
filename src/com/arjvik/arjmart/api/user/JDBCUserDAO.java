package com.arjvik.arjmart.api.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.inject.Inject;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;
import com.mysql.jdbc.Statement;

public class JDBCUserDAO implements UserDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCUserDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public User getUser(int ID) throws UserNotFoundException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from User where UserID=?");
			statement.setInt(1, ID);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new UserNotFoundException(ID);
			return new User(resultSet.getInt("UserID"), resultSet.getString("Email"), resultSet.getString("Password"), resultSet.getString("CreditCardNumber"));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public int addUser(User user) throws UserAlreadyExistsException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("insert into User (Email, Password, CreditCardNumber) values (?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getCreditCardNumber());
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new UserAlreadyExistsException(user.getEmail(), e);
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void editUserCreditCardNumber(int ID, User user) throws UserNotFoundException, DatabaseException {
		getUser(ID);
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("update User set CreditCardNumber = ? where UserID = ?");
			statement.setString(1, user.getCreditCardNumber());
			statement.setInt(2, ID);
			statement.executeUpdate();
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public boolean authenticate(User user) throws DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select UserID from User where Email = ? and Password = ?");
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				user.setID(resultSet.getInt("UserID"));
				return true;
			} else {
				return false;
			}
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteUser(int ID) throws UserNotFoundException, DatabaseException {
		getUser(ID);
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("delete from User where UserID = ?");
			statement.setInt(1, ID);
			if(statement.executeUpdate()==0)
				throw new UserNotFoundException(ID);
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
