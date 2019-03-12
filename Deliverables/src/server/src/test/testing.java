package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import storage.Atleta;
import storage.Bean;
import storage.Campetto;
import storage.CampettoDAO;
import storage.DriverManagerConnectionPool;
import storage.StorageFacade;

public class testing {

	public testing() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		/*StorageFacade s = new StorageFacade();
		
		Collection<Bean> campetti = s.getLista("storage.Campetto");
		 Iterator<Bean> iter3 = campetti.iterator();
		 while(iter3.hasNext()) {
			 Campetto uncampetto = (Campetto) iter3.next();

			 System.out.println(uncampetto.getID()+" "+uncampetto.getCitta()+" "+uncampetto.getNome());
				 
		}*/	 
		
		try {
			getAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		 
		 

	}

	public synchronized static Collection<Bean> getAll() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<Bean> atleti = new LinkedList<Bean>();

		String selectSQL = "SELECT * FROM atleta";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Atleta bean = new Atleta();
				System.out.println("Inizio prima di settarlo:"+bean.getID());

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
	




}







