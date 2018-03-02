package com.arjvik.arjmart.api;

public class JDBCAuthenticationDAO implements AuthenticationDAO {
	
	private ConnectionFactory connectionFactory;
	
	@Inject
	public JDBCAuthenticationDAO(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public int authenticate(String credentials) throws AuthenticationFailedException, DatabaseException {
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
		// TODO Auto-generated method stub

	}

}
