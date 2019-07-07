package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;


/* INSERT INTO atleta (Email,ID,Password, Nome, Cognome, DataDiNascita, Residenza, RuoloPreferito, Username)
  VALUES ('pierlu1997@libero.it',1,'miaomiao01','Pierluigi','Liguori','1997-09-07','Polla','Attaccante','Fernet'); 
  
   INSERT INTO atleta (Email,Password, Nome, Cognome, DataDiNascita, Residenza, RuoloPreferito, Username) 
   VALUES ('giosualdo@libero.it','baobab01','Giosualdo','Spinaci','1990-09-17','Milano','Attaccante','cipollino90');  */


public  class AtletaDAO implements Fut5alDAO{

	private final String TABLE_NAME = "atleta";

	
	@Override
	public synchronized Collection<Bean> getAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> atleti = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Atleta bean = new Atleta();
				System.out.println(bean.getID());

				bean.setEmail(rs.getString("Email"));
				bean.setID(rs.getInt("ID"));
				bean.setPassword(rs.getString("Password"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
				bean.setData(rs.getString("DataDiNascita"));
				bean.setResidenza(rs.getString("Residenza"));
				bean.setRuolo(rs.getString("RuoloPreferito"));
				bean.setUsername(rs.getString("Username"));

				atleti.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		
		
		return atleti;
	}
	
	
	

	@Override
	public synchronized Bean getByID(int id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Atleta bean = new Atleta();

		String selectSQL = "SELECT * FROM " + this.TABLE_NAME + " WHERE ID = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id); //PRIMA ERA 2, mmh!!!!!!!!!!!!!!

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bean.setEmail(rs.getString("Email"));
				bean.setID(rs.getInt("ID"));
				bean.setPassword(rs.getString("Password"));
				bean.setNome(rs.getString("Nome"));
				bean.setCognome(rs.getString("Cognome"));
				bean.setData(rs.getString("DataDiNascita"));
				bean.setResidenza(rs.getString("Residenza"));
				bean.setRuolo(rs.getString("RuoloPreferito"));
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
	public  void update(Bean b) {

		
	}

	@Override
	public synchronized void add(Bean b) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		String insertSQL = "INSERT INTO " + TABLE_NAME
				+ " (Email,Password, Nome, Cognome, DataDiNascita, Residenza, RuoloPreferito, Username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		
		Atleta a = (Atleta) b;
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, a.getEmail());
			preparedStatement.setString(2, a.getPassword());
			preparedStatement.setString(3, a.getNome());
			preparedStatement.setString(4, a.getCognome());
			preparedStatement.setString(5, a.getData());
			preparedStatement.setString(6, a.getResidenza());
			preparedStatement.setString(7, a.getRuolo());
			preparedStatement.setString(8, a.getUsername());

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
		
		
	}

	
}
