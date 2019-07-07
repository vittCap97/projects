package test;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import storage.Atleta;
import storage.Bean;
import storage.Campetto;
import storage.Gestore;
import storage.Gioca;
import storage.Partita;
import storage.StorageFacade;

public class StorageFacadeTest extends TestCase {
	
	private StorageFacade storage;
	private Atleta a;
	private Campetto camp;
	
	
	 public StorageFacadeTest(String name) {
		 super(name);
	}
	 
	 @Override
	protected void setUp() throws Exception {	
			storage = StorageFacade.getInstance();
			a = new Atleta("Aldo@gmail.com","aldoaldo","Aldo","Baglio","1887-12-12","Firenze", "Portiere", "Bar87");
			camp = new Campetto("Campo Rosso", "15$", "Napoli", 3, true);

	 }
	
	@Test
	public void GetOggetto_AtletaValido(){ //ERROR 
		
		Atleta a1 = (Atleta) storage.getOggetto("storage.Atleta", 10); //L'id di Aldo nel DB, è 10
		Assert.assertEquals(a, a1);
		
	}
	
	@Test
	public void GetOggetto_GestoreNonValido(){ //PASS		
		//Con id non valido (<1)
		Gestore g1 = (Gestore) storage.getOggetto("storage.Gestore", -1);
		Assert.assertNull(g1);

	}
	

	@Test
	public void Get_OggettoGiocaNonValido(){ //ERROR mi esce un bean con ID=0 anzichè null		
		//Con id non valido (< ultimoIDinserito)
		Gioca g1 = (Gioca) storage.getOggetto("storage.Gioca", 999);
		Assert.assertNull(g1);

	}
	
	
	@Test
	public void Get_Oggetto_CampettoValido() { //PASS		
		Campetto a1 = (Campetto) storage.getOggetto("storage.Campetto", 2); 
		Assert.assertEquals(camp, a1);
		
	}
	
	
	@Test
	public void Get_OggettoPartitaNonValido() { //PASS		
		//Con id non valido (<1)
		Partita g1 = (Partita) storage.getOggetto("storage.Partita", -1);
		Assert.assertNull(g1);
		
	}
	
	
	@Test
	public void Get_OggettoNullNonValido() { //PASS		
		Assert.assertNull(storage.getOggetto("Albicocca", 999));
	}
	
	
	
	@Test
	public void GetLista_atleta() { //PASS	
		
		Collection<Bean> lista = storage.getLista("storage.Atleta");
		
		for(Bean b : lista) {  //ritorna effettivamente una lista di atleti
			assertTrue(b.getClass().getName().equals("storage.Atleta"));
		}
	}
	
	
	@Test
	public void GetLista_gestore() { // PASS	
		
		Collection<Bean> lista = storage.getLista("storage.Gestore");
		
		for(Bean b : lista) {  //ritorna effettivamente una lista di atleti
			assertTrue(b.getClass().getName().equals("storage.Gestore"));
		}
	}
	
	@Test
	public void GetLista_gioca() {//PASS 
			
		Collection<Bean> lista = storage.getLista("storage.Gioca");
		
		for(Bean b : lista) {  //ritorna effettivamente una lista di atleti
			assertTrue(b.getClass().getName().equals("storage.Gioca"));
		}
	}
	
	
	@Test
	public void GetLista_campetto() { // PASS
			
		Collection<Bean> lista = storage.getLista("storage.Campetto");
		
		for(Bean b : lista) {  //ritorna effettivamente una lista di atleti
			assertTrue(b.getClass().getName().equals("storage.Campetto"));
		}
	}
	
	
	@Test
	public void GetLista_partita() { //PASS
			
		Collection<Bean> lista = storage.getLista("storage.Partita");
		
		for(Bean b : lista) {  //ritorna effettivamente una lista di atleti
			assertTrue(b.getClass().getName().equals("storage.Partita"));
		}
	}
	

	
	@Test
	public void GetLista_ERR() { //ERROR: ritornava una lista vuota e non null
				
		Collection<Bean> lista = storage.getLista("Albicocca");
		
		assertNull(lista);
		
	}
	
	
}
