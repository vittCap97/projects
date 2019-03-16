package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import storage.Bean;
import storage.Campetto;
import storage.Gioca;
import storage.Partita;
import storage.StorageFacade;

/**
 * Servlet implementation class CercaPartiteServlet
 */
@WebServlet("/CercaPartiteServlet")
public class CercaPartiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
	private String tipo_partitaDaCercare;
	private String clientPartecipazione;
	private String emailutente;
	private boolean GiocoInQuesta;
	private static int chiamate=0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CercaPartiteServlet() {
		 storage = new StorageFacade();
		 GiocoInQuesta = false;
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
		System.out.println("\n\n"+(chiamate++)+"voglio una lista di partite "+request.getParameter("tipo_partite")+" Partecipazione:"+request.getParameter("miapartecipazione")+" utente:"+request.getParameter("MyEmail"));
		clientPartecipazione = request.getParameter("miapartecipazione");
		tipo_partitaDaCercare = request.getParameter("tipo_partite");
		emailutente = request.getParameter("MyEmail");
		
		
         Collection<Bean> partite = storage.getLista("storage.Partita");
         //Per tutte le partite...devo visualizzare:
		 for(Bean b: partite) {
			 
	         JsonObject obj = new JsonObject();
			 Partita p = (Partita) b;
			 
			 if(p.getStatoPartita().equals(tipo_partitaDaCercare)) {
				 
	         obj.addProperty("Ora", p.getOra());
	         obj.addProperty("Data", p.getData());
	         obj.addProperty("CodicePartita", p.getID());
			 

			//info Campetto relativo a quella partita
			 Campetto campetto = (Campetto) storage.getOggetto("storage.Campetto", p.getCampetto_ID());
		     obj.addProperty("NomeCampetto",campetto.getNome());
		     obj.addProperty("Citta", campetto.getCitta());
		     obj.addProperty("Tariffa", campetto.getTariffa());
		         

			 //num Partecipanti a tale partita
			 int num_partecipanti = 0;
			 Collection<Bean> giocatori = (Collection<Bean>)storage.getLista("storage.Gioca");

			for(Bean giocatore : giocatori) { //calcolo num partecipanti x quella partita
				Gioca gioc = (Gioca) giocatore;
			 	if(gioc.getPartita_ID() == p.getID() && (gioc.getStatoInvito().equals("Sicuro") || gioc.getStatoInvito().equals("riservato"))) {
						 num_partecipanti++;
						 	//controllo se il client gioca in questa partita
						 	if(gioc.getAtleta_Email().equals(emailutente)) GiocoInQuesta = true;
					 }

			}
				 
		    obj.addProperty("NumPartecipanti", num_partecipanti);
		         
		    String PartitaItemDaInviare = obj.toString(); //Formatta l'insieme di info in un unica stringa
		    PrintWriter out = response.getWriter();
		    
		    //Se voglio le mie partite, e sono presente in questa partita, invio.
		    if(clientPartecipazione.equals("si") & GiocoInQuesta) {
			    if(PartitaItemDaInviare!=null) {
				     out.println(PartitaItemDaInviare); }}//Invia la partita item
		    
		    //Se non voglio vedere le mie partite e non gioco in questa, la voglio vedere
		    if(clientPartecipazione.equals("no") & !GiocoInQuesta) {
			    if(PartitaItemDaInviare!=null) {
				     out.println(PartitaItemDaInviare); }
			    }
		    
			 if (clientPartecipazione.equals("indifferente")){	//se mi è indifferente..
				    if(PartitaItemDaInviare!=null) {
					     out.println(PartitaItemDaInviare); }
			 }
		    
		    
			System.out.println("partita :"+PartitaItemDaInviare +"Gioco in questa="+GiocoInQuesta);
			GiocoInQuesta = false;
			 }
		 }		
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
