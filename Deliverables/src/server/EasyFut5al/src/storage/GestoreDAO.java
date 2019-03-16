package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class GestoreDAO implements Fut5alDAO {
	
	private final String TABLE_NAME = "gestore";


	@Override
	public synchronized Collection<Bean> getAll() throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> gestori = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Gestore bean = new Gestore();
				bean.setEmail(rs.getString("Email"));
				bean.setID(rs.getInt("ID"));
				bean.setPassword(rs.getString("Password"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
				bean.setCampetto_ID(rs.getInt("Campetto_ID"));
				bean.setUsername(rs.getString("Username"));
				gestori.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
		
		return gestori;
	}

	
	
	@Override
	public synchronized Bean getByID(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Gestore bean = new Gestore();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME + " WHERE ID = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(2, id);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setEmail(rs.getString("Email"));
				bean.setID(rs.getInt("ID"));
				bean.setPassword(rs.getString("Password"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
				bean.setCampetto_ID(rs.getInt("Campetto_ID"));
				bean.setUsername(rs.getString("Username"));
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return bean;
	}

	
	
	
	
	
	
	@Override
	public  synchronized void update(Bean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void add(Bean b)  throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + TABLE_NAME
				+ " (Email,Password, Nome, Cognome, Username, Campetto_ID) VALUES (?, ?, ?, ?, ?,?)";

		
		Gestore a = (Gestore) b;
	
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, a.getEmail());
			preparedStatement.setString(2, a.getPassword());
			preparedStatement.setString(3, a.getNome());
			preparedStatement.setString(4, a.getCognome());
			preparedStatement.setString(5, a.getUsername());
			preparedStatement.setInt(6, a.getCampetto_ID());
			

			preparedStatement.executeUpdate();

			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
	}

	@Override
	public synchronized void remove(int id) {
		// TODO Auto-generated method stub
		
	}

}
