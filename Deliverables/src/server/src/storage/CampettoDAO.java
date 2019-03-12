package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/*
 *  INSERT INTO Campetto(Nome, Tariffa, Città, Valutazione, Agibilita) VALUES
 *   ('GoldenField', '5 Euro', 'Modena', '3', 1);
 * */

public class CampettoDAO implements Fut5alDAO{

	private final String TABLE_NAME = "campetto";

	@Override
	public synchronized Collection<Bean> getAll() throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> campetti = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Campetto bean = new Campetto();
				bean.setID(rs.getInt("ID"));
				bean.setNome(rs.getString("Nome"));
				bean.setTariffa(rs.getString("Tariffa"));
				bean.setCitta(rs.getString("Città"));
				bean.setValutazione(rs.getFloat("Valutazione"));
				bean.setAgibilita(rs.getBoolean("Agibilita"));
				campetti.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
		return campetti;
	}
	
	

	@Override
	public synchronized Bean getByID(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Campetto bean = new Campetto();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME + " WHERE ID = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setID(rs.getInt("ID"));
				bean.setNome(rs.getString("Nome"));
				bean.setTariffa(rs.getString("Tariffa"));
				bean.setCitta(rs.getString("Città"));
				bean.setValutazione(rs.getFloat("Valutazione"));
				bean.setAgibilita(rs.getBoolean("Agibilita"));

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
				+ " (Nome, Tariffa, Città, Agibilita,Valutazione) VALUES (?, ?, ?, ?,?)";

		
		Campetto a = (Campetto) b;
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, a.getNome());
			preparedStatement.setString(2, a.getTariffa()+"$");
			preparedStatement.setString(3, a.getCitta());
			preparedStatement.setBoolean(4, false);
			preparedStatement.setString(5, "0");
			
			

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
