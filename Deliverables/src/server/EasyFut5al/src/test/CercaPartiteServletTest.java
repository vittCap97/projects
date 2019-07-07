package test;



import java.util.Collection;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;

import junit.framework.TestCase;
import middleware.CercaPartiteServlet;
import storage.Bean;
import storage.Gioca;
import storage.Partita;
import storage.StorageFacade;


public class CercaPartiteServletTest extends TestCase{
	
	private ServletTestModule tester;
	private WebMockObjectFactory factory;
	private StorageFacade storage;
	private Collection<Bean> partite;
	private Collection<Bean> giocatori;


	 public CercaPartiteServletTest(String name){
		 super(name);
	}
	 
	 @Override
	protected void setUp() throws Exception {	
			
			factory = new WebMockObjectFactory();
			tester = new ServletTestModule(factory);
			storage = StorageFacade.getInstance();
			partite =  storage.getLista("storage.Partita"); //Partite che mi ritorna Storage
			giocatori = (Collection<Bean>)storage.getLista("storage.Gioca");


	 }
	
	@Test
	public void Mia_DaGiocare_Valid(){
		
		String TipoPartecipazione = "si";
		String TipoPartite = "DaGiocare";
		String Email = "dino@gmail.com";

		tester.addRequestParameter("miapartecipazione", TipoPartecipazione );
		tester.addRequestParameter("tipo_partite", TipoPartite);
		tester.addRequestParameter("MyEmail", Email);

		tester.createServlet(CercaPartiteServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		String testo[] = output.split("\\r?\\n"); //Partite che mi ritorna la servlet
	
		int i = 0;
		for(Bean b: partite) {
			Partita a = (Partita) b;
			if(a.getStatoPartita().equals(TipoPartite)) { //Cerco le partite Da Giocare
			//Per capire se partecipo a quella partita 
			for(Bean giocatore : giocatori) {
				Gioca gioc = (Gioca) giocatore;
				if(gioc.getPartita_ID() == a.getID() && (gioc.getStatoInvito().equals("Sicuro") || gioc.getStatoInvito().equals("riservato"))) {
					if(gioc.getAtleta_Email().equals(Email)) {
					
						JsonObject j = new JsonParser().parse(testo[i]).getAsJsonObject();
						assertTrue(a.getOra() == j.get("Ora").getAsInt());
						assertEquals(a.getData(), j.get("Data").getAsString());
						assertTrue(a.getID() == j.get("CodicePartita").getAsInt());
						i++;}}}	}}
	}
	
	
	
	
	
	@Test
	public void InSos_NonValid() {
		
		//Vuol dire che devo cercare tutte le partite in sospeso dove non partecipa
		//e poichè la email non è valida, devo cercare Tutte le partite in sospeso.
		String TipoPartecipazione = "no";
		String TipoPartite = "InSospeso";
		String Email = "";

		tester.addRequestParameter("miapartecipazione", TipoPartecipazione );
		tester.addRequestParameter("tipo_partite", TipoPartite);
		tester.addRequestParameter("MyEmail", Email);

		tester.createServlet(CercaPartiteServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		String testo[] = output.split("\\r?\\n"); //Partite che mi ritorna la servlet
				
		int i = 0;
		for(Bean b: partite) {
			Partita a = (Partita) b;
			if(a.getStatoPartita().equals(TipoPartite)) { //Cerco le partite in sospeso					
						JsonObject j = new JsonParser().parse(testo[i]).getAsJsonObject();
						assertTrue(a.getOra() == j.get("Ora").getAsInt());
						assertEquals(a.getData(), j.get("Data").getAsString());
						assertTrue(a.getID() == j.get("CodicePartita").getAsInt());
						i++;}
		}	
	}	
	
	
	
	@Test
	public void Terminata_Valid() {
		
		String TipoPartecipazione = "indifferente";
		String TipoPartite = "Terminata";
		String Email = "pierlu1997@libero.it";

		tester.addRequestParameter("miapartecipazione", TipoPartecipazione );
		tester.addRequestParameter("tipo_partite", TipoPartite);
		tester.addRequestParameter("MyEmail", Email);

		tester.createServlet(CercaPartiteServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		String testo[] = output.split("\\r?\\n"); //Partite che mi ritorna la servlet
				
		int i = 0;
		for(Bean b: partite) {
			Partita a = (Partita) b;
			if(a.getStatoPartita().equals(TipoPartite)) { //Cerco le partite Terminate				
						JsonObject j = new JsonParser().parse(testo[i]).getAsJsonObject();
						assertTrue(a.getOra() == j.get("Ora").getAsInt());
						assertEquals(a.getData(), j.get("Data").getAsString());
						assertTrue(a.getID() == j.get("CodicePartita").getAsInt());
						i++;}
		}	
		
	}   
	
	
	@Test
	public void Error_NonValid() {

		String TipoPartecipazione = "Albicocca";
		String TipoPartite = "Albicocca";
		String Email = "";

		tester.addRequestParameter("miapartecipazione", TipoPartecipazione );
		tester.addRequestParameter("tipo_partite", TipoPartite);
		tester.addRequestParameter("MyEmail", Email);

		tester.createServlet(CercaPartiteServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		
		assertTrue(output.length() == 0);
		
	}
	
}
