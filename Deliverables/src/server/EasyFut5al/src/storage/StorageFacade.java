package storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class StorageFacade{
	
	private AtletaDAO atletaDao;
	private CampettoDAO campettoDao;
	private GestoreDAO gestoreDao;
	private PartitaDAO partitaDao;
	private GiocaDAO giocaDao;
	
	
	public StorageFacade() {
		atletaDao = new AtletaDAO();
		campettoDao = new CampettoDAO();
		gestoreDao = new GestoreDAO();
		partitaDao = new PartitaDAO();
		giocaDao = new GiocaDAO();
	}


	
	public int Salva(Bean b) {
		
		
		String type = b.getClass().getName();
		
		switch(type) {
		case "storage.Atleta":
			try {
				atletaDao.add(b);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 1;
				
			}
			break;

		case "storage.Campetto":
			try {
				campettoDao.add(b);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 1;
				
			}
			break;
			
		case "storage.Gestore":
			try {
			gestoreDao.add(b);}
			catch (SQLException e) {
				e.printStackTrace();
				return 1;

			}
			break;
			
		case "storage.Gioca":
			try {
				giocaDao.add(b);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 1;

			}
			break;
			
		case "storage.Partita":
			try {
				partitaDao.add(b);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 1;

			}
			break;

		default:
			System.out.println("Tipo oggetto non trovato");
			
		}	
		
		return 0;
	}
	

	
	
	
	public Collection<Bean> getLista(String type){
		
		Collection<Bean> beans =  new LinkedList<Bean>();
		
		try {
		switch(type) {
		case "storage.Atleta":
			beans= atletaDao.getAll();
			break;

		case "storage.Campetto":
			beans = campettoDao.getAll();
			break;
	
		case "storage.Gestore":
			beans = gestoreDao.getAll();
			break;
			
		case "storage.Gioca":
			beans = giocaDao.getAll();
			break;

			
		case "storage.Partita":
			beans = partitaDao.getAll();
			break;

		default:
			System.out.println("Tipo oggetto non trovato");
				
		}
		
		}catch (Exception e) {
			e.getStackTrace();
			System.out.println("Impossibile completare l'operazione getLista");
		}
		
		return beans;
		
	}
	
	
	
	
	public Bean getOggetto(String type, int id) {
		
		Bean b = null;
		
		try {
		switch(type) {
		case "storage.Atleta":
			b = atletaDao.getByID(id);
			break;

		case "storage.Campetto":
			b = campettoDao.getByID(id);
			break;

			
		case "storage.Gestore":
			b = gestoreDao.getByID(id);
			break;

			
		case "storage.Gioca":
			b =giocaDao.getByID(id);
			break;
			
		case "storage.Partita":
			b =partitaDao.getByID(id);
			break;
			
			
		default:
			System.out.println("Tipo oggetto non trovato");
				
		}	
		
		}catch (Exception e) {
			e.getStackTrace();
			System.out.println("Impossibile completare l'operazione getOggetto");
		}
		

		return b;
		
	}
	
	
	
	//id oggetti nel db da aggiornare, b= bean che lo rimpiazzerà
	public void aggiorna(Bean b) {
		String type = b.getClass().getName();
		
		switch(type) {
		case "storage.Atleta":
			atletaDao.update(b);
			break;

		case "storage.Campetto":
			try {
				campettoDao.update(b);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "storage.Gestore":
			gestoreDao.update(b);
			break;
			
		case "storage.Gioca":
			giocaDao.update(b);
			break;
			
		case "storage.Partita":
			try {
				partitaDao.update(b);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			
		default:
			System.out.println("Tipo oggetto non trovato");

		}
	}
	
	
	public void elimina(Bean b) {
		String type = b.getClass().getName();
		
		switch(type) {
		case "storage.Atleta":
			atletaDao.remove(b.getID());
			break;

		case "storage.Campetto":
			campettoDao.remove(b.getID());
			break;
			
		case "storage.Gestore":
			gestoreDao.remove(b.getID());
			break;
			
		case "storage.Gioca":
			try {
				giocaDao.remove(b.getID());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "storage.Partita":
			partitaDao.remove(b.getID());
			break;
			
			
		default:
			System.out.println("Tipo oggetto non trovato");

		}
		
	    
		
	}
	
	
	

}
