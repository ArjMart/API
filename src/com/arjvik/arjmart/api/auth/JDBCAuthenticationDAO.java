package com.arjvik.arjmart.api.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.glassfish.jersey.internal.util.Base64;

import com.arjvik.arjmart.api.ConnectionFactory;
import com.arjvik.arjmart.api.DatabaseException;

public class JDBCAuthenticationDAO implements AuthenticationDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCAuthenticationDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public int authenticate(String credentials) throws AuthenticationFailedException, DatabaseException {
		if(credentials == null)
			throw new AuthenticationFailedException();
		String decoded = Base64.decodeAsString(credentials.substring(6));
		String[] credentialArray = decoded.split(":");
		String email = credentialArray[0];
		String password = credentialArray[1];
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select UserID from User where Email = ? and Password = ?");
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getInt("UserID");
			} else {
				throw new AuthenticationFailedException();
			}
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void authorize(int userID, String role) throws AuthorizationFailedException, DatabaseException {
		try (Connection connection = connectionFactory.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("select * from UserRoles where UserID = ? and (Role = ? or Role = '"+Role.SUPER_ADMIN+"')");
			statement.setInt(1, userID);
			statement.setString(2, role);
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
				throw new AuthorizationFailedException(userID, role);
		}catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

}
