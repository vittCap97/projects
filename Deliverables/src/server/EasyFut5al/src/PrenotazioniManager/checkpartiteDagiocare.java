package PrenotazioniManager;

import java.util.Collection;
import java.util.LinkedList;


import storage.Bean;
import storage.Partita;
import storage.StorageFacade;

public class  checkpartiteDagiocare implements Runnable {
	
	private Collection<Bean> partite;
	private StorageFacade storage;
	
	
	public  checkpartiteDagiocare(){
		System.out.println("Inizializza ricerca partite pronte per giocare.. ");
		partite =  new LinkedList<Bean>();
		storage = new StorageFacade();
	
		
	}
	
	@Override
	//avviene una volta al secondo
	public synchronized void run() {
		
		partite = storage.getLista("partita");
		
		for (Bean partita : partite) {
			Partita p = (Partita) partita;
			if(p.getStatoPartita().equals("DaGiocare")) {
				System.out.println("Trovata partita"+p.getID()+" da giocare ");
			}
				
		}
	}
	
	

	

}