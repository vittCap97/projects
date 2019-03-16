package storage;

import java.io.Serializable;

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
	
	
}
