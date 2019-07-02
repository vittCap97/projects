package storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 
 * @author Fernet
 * Storage Facade fa uso del pattern Facade e fornisce bl bla
 * 
 *
 */
//SINGLETON
public class StorageFacade{
	
	private static volatile StorageFacade instance;

	
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
	
	public static StorageFacade getInstance() {
		if (instance == null) {
			synchronized (StorageFacade.class) {
				if (instance == null)
					instance = new StorageFacade();
			}
		}
		return instance;
	}


	/**
	 * Tal metodo ritorna un bean
	 * Context Registration:: Student insertRegistrationStudent(Dati); <br>
pre: not isNull(Dati) && isFormatDataCorrect(Dati)==true<br>

Context Registration:: Secretary insertRegistrationSecretary(Dati); <br>
pre: not isNull(Dati) && isFormatDataCorrect(Dati)==true && 
isDataValid(dati)==true

Context Registration:: Council
insertRegistrationConsiglioDidattico(Dati); 
pre: not isNull(Dati) && isFormatDataCorrect(Dati)==true && 
isDataValid(dati)==true

	 * @param b è il bean da salvare
	 * @return il codice di controllo
	 * 
	 */
	
	public synchronized int  Salva(Bean b) {
		
		
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
			
		// Non ci possono essere due istanze "gioca" per un atleta su stessa partita
		// non ci devono essere più di 10 persone per partita
		case "storage.Gioca":
			try {
				if(IsNonRidondate(b) && MinDi10(b)) giocaDao.add(b);
				else { 
					System.out.println("Vincoli violati, ma no problem, non salvo");
					return 0;
				}
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
	

	//Vincoli su gioca*******************
	public synchronized boolean MinDi10(Bean b) {
		if(giocaDao.NumGiocDellapartita(b) > 10) return false;
		else return true;
	}
	

	
	public synchronized  boolean IsNonRidondate(Bean b) {
		if(giocaDao.findRidondance(b) >1) return false;
		return true;
		
	}
	//***********************************
	

	public synchronized Collection<Bean> getLista(String type){
		
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
	
	
	
	
	public synchronized Bean getOggetto(String type, int id) {
		
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
	public synchronized void aggiorna(Bean b) {
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
	
	
	public synchronized void  elimina(Bean b) {
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
	
	
	
	
	public synchronized int  IDultimaPartita() {
		return partitaDao.lastIDAdd();
	}
	
	
	

}
