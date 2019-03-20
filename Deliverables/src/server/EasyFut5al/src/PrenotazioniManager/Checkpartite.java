package PrenotazioniManager;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;


import storage.Bean;
import storage.Gioca;
import storage.Partita;
import storage.StorageFacade;

public class  Checkpartite implements Runnable {
	
	private Collection<Bean> partite;
	private StorageFacade storage;
	private boolean pronta;
	
	
	public  Checkpartite(){
		System.out.println("Inizializza ricerca partite pronte per giocare.. ");
		partite =  new LinkedList<Bean>();
		storage = new StorageFacade();
	
		
	}
	
	@Override
	//avviene una volta al secondo
	public synchronized void run() {
		
		partite = storage.getLista("storage.Partita");
		
		for (Bean partita : partite) { // Per ogni partita...
			
			Partita p = (Partita) partita;
			
			if(p.getStatoPartita().equals("InSospeso") || p.getStatoPartita().equals("DaGiocare")) {
				pronta = check10sicuri(p.getID());//Controlla se ci sono 10 "sicuri"
				
				if(pronta) { 
					p.setStatoPartita("DaGiocare");
					storage.aggiorna(p);
					
				}else {
					p.setStatoPartita("InSospeso");
					storage.aggiorna(p);
				}
				
				//Confronta con data e ora di oggi 
				Calendar c = Calendar.getInstance();
	            String mese = String.format("%02d", c.get(Calendar.MONTH)+1);
	            String giorno = String.format("%02d", c.get(Calendar.DAY_OF_MONTH));
	            int Ora = c.get(Calendar.HOUR);
	            String DataDiOggi = c.get(Calendar.YEAR)+"-"+mese+"-"+giorno;	
				//Se tra un ora si doveva giocare la partita
				if(p.getData().equals(DataDiOggi) && (p.getOra()-1)==Ora) {
					System.out.println("La partita sta per iniziare tra un ora!!");
					if(p.getStatoPartita().equals("InSospeso")) p.setStatoPartita("Terminata");
					if(p.getStatoPartita().equals("DaGiocare")) p.setStatoPartita("Cancellata");
				}
			}
		}
	}

	public boolean check10sicuri(int idPartita) {
        
		int sicuri = 0;
		
		Collection<Bean> partecipanti = storage.getLista("storage.Gioca");
		for(Bean g: partecipanti) {
			Gioca partecipante= (Gioca) g;
			if(partecipante.getStatoInvito().equals("Sicuro")) sicuri++;
		}
		
		if(sicuri == 10) return true;
		else return false;

	}
	
	

	

}