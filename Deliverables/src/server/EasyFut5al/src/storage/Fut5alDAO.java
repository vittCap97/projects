package storage;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interfaccia comune a tutti i bean per le operazioni DAO
 * @author Fernet
 *
 */
public interface Fut5alDAO{
	

	/**
	 * @return una collezione di bean ricavati dalla tabella
	 * @throws SQLException 
	 */
	public Collection<Bean> getAll() throws SQLException; 
	
	/**
	 * @param id del record nella tabella
	 * @return Bean riempito  con i campi di quel record
	 * @throws SQLException 
	 */
	public Bean getByID(int id) throws SQLException; 
	

	/**
	 * 
	 * @param id l'identificativo dell'oggetto da modificare
	 * @param b il bean riempito con i parametri modificati che sostituirà il record indicato da id
	 * @throws SQLException 
	 */
	public void update(Bean b) throws SQLException; 
	
	/**
	 * @param b è il bean che riempirà un nuovo record
	 * @throws SQLException 
	 */
	public void add(Bean b) throws SQLException; 
	
	/**
	 * @param identificativo del record che verrà eliminato
	 * @throws SQLException 
	 */
	public void remove(int id) throws SQLException;

}
