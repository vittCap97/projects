package storage;

import java.io.Serializable;


/**
 * Rappresenta un modello per ogni oggetto persistente
 * @author Fernet
 *
 */

public abstract class Bean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5766643891879398863L;
	private  int ID=0;


	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

	
	
	
	@Override
	public String toString() {
		return "Bean [ID=" + ID + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bean other = (Bean) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
	
	
	
}
