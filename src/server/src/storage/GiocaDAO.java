package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class GiocaDAO implements Fut5alDAO{

	private final String TABLE_NAME = "gioca";
	
	
	@Override
	public synchronized Collection<Bean> getAll() throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> giochi = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Gioca bean = new Gioca();
				bean.setAtleta_Email(rs.getString("Atleta_Email"));
				bean.setPartita_ID(rs.getInt("Partita_ID"));
				bean.setStatoInvito(rs.getString("StatoInvito"));
				giochi.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
		
		return giochi;
	}
	

	@Override
	public Bean getByID(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Gioca bean = new Gioca();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME + " WHERE ID = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setAtleta_Email(rs.getString("Atleta_Email"));
				bean.setPartita_ID(rs.getInt("Partita_ID"));
				bean.setStatoInvito(rs.getString("StatoInvito"));


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
	public synchronized void update(int id, Bean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void add(Bean b) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + TABLE_NAME
				+ " (Atleta_Email, Partita_ID, StatoInvito) VALUES (?, ?, ?)";

		
		Gioca a = (Gioca) b;
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, a.getAtleta_Email());
			preparedStatement.setInt(2, a.getPartita_ID());
			preparedStatement.setString(3, a.getStatoInvito());
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
