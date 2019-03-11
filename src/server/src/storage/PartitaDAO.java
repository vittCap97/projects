package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;



/*
 * INSERT INTO Partita(Data, Ora, StatoPartita) 
 * VALUES ('2019-01-23', 5, 'DaGiocare');*/
public class PartitaDAO implements Fut5alDAO {
	
	private final String TABLE_NAME = "partita";

	@Override
	public synchronized Collection<Bean> getAll() throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> partite = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Partita bean = new Partita();
				bean.setID(rs.getInt("ID"));
				bean.setData(rs.getString("Data"));
				bean.setOra(rs.getInt("Ora"));
				bean.setStatoPartita(rs.getString("StatoPartita"));
				bean.setCampetto_ID(rs.getInt("Campetto_ID"));
				partite.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
		
		return partite;
	}

	@Override
	public synchronized Bean getByID(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Partita bean = new Partita();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME + " WHERE ID = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setID(rs.getInt("ID"));
				bean.setData(rs.getString("Data"));
				bean.setOra(rs.getInt("Ora"));
				bean.setStatoPartita(rs.getString("StatoPartita"));
				bean.setCampetto_ID(rs.getInt("Campetto_ID"));

			}

		}
		catch(Exception e){e.printStackTrace();}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} 
			catch(Exception e){e.printStackTrace();}
			finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return bean;
	}

	@Override
	public synchronized  void update(int id, Bean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void add(Bean b) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + TABLE_NAME
				+ " (Data, Ora, StatoPartita, Campetto_ID) VALUES (?, ?, ?, ?)";

		
		Partita a = (Partita) b;
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, a.getData());
			preparedStatement.setInt(2, a.getOra());
			preparedStatement.setString(3, a.getStatoPartita());
			preparedStatement.setInt(4, a.getCampetto_ID());
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
