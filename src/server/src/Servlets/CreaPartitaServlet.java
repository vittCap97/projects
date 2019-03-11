package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.Atleta;
import storage.Bean;
import storage.Campetto;
import storage.Gioca;
import storage.Partita;
import storage.StorageFacade;

/**
 * Servlet implementation class CreaPartitaServlet
 */
@WebServlet("/CreaPartitaServlet")
public class CreaPartitaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> partecipanti;
	private StorageFacade storage;
	private int check;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreaPartitaServlet() {
        super();
         partecipanti = new ArrayList<>();
		 storage = new StorageFacade();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        InputStream stream = request.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
        String s = "";
        
        String atletaOrganizzatore =  buffer.readLine();
        partecipanti.add(atletaOrganizzatore);
        
        String orario = buffer.readLine();
        String data = buffer.readLine();
        String nomeCampetto = buffer.readLine();
        
		//Nome invitati
        while ((s = buffer.readLine()) != null) {
        	partecipanti.add(s);
        }

        System.out.println("Ricevuto da "+atletaOrganizzatore+" : " +orario+","+ data+","+nomeCampetto);
	    PrintWriter out = response.getWriter();

        //creazione della partita nel db
        Collection<Bean> campetti =  storage.getLista("storage.Campetto");
        for(Bean campetto : campetti) {
        	Campetto c = (Campetto) campetto;
        	if(c.getNome().equals(nomeCampetto)) {
                Partita nuovaPartita = new Partita();
                nuovaPartita.setData(data);
                nuovaPartita.setOra(Integer.parseInt(orario));
                nuovaPartita.setCampetto_ID(c.getID());
                nuovaPartita.setStatoPartita("InSospeso");
                storage.Salva(nuovaPartita); //Salvataggio partita
               
                Collection<Bean> partite =  storage.getLista("storage.Partita"); // mi serve solo per prendere il numero di partite salvate che � uguale all'ultimo ID aggiunto
                //Inizio a far partecipare l'atleta organizzatore
                Gioca g1 = new Gioca();
                g1.setAtleta_Email(partecipanti.get(0));
                g1.setStatoInvito("Sicuro");
                g1.setPartita_ID(partite.size()); //mmmh
                storage.Salva(g1); //Salva partecipazione dell'Organizzatore
                partecipanti.remove(atletaOrganizzatore); //ormai l'ho gi� aggiunto

                //poi faccio partecpare gli invitati
                for(String Nickpartecipante: partecipanti) {
                	Gioca g = new Gioca();
                	String emailPartecipante = findEmail(Nickpartecipante);
                	g.setAtleta_Email(emailPartecipante);
                    g.setStatoInvito("riservato");
                    g.setPartita_ID(partite.size()); //mmmh
                    check = storage.Salva(g);}
                	if(check == 1)out.println("Partita registrata con successo!");
                	return;
                }	

        	}
        
        out.println("Errore registrazione partita"); //Invia la partita item 

       
	
	}

	private String findEmail(String nickpartecipante) {
        
		String EmailPartecipante = null;
		
        //Ricavo email invitati
        Collection<Bean> atleti =  storage.getLista("storage.Atleta"); // mi serve solo per prendere il numero di partite salvate che � uguale all'ultimo ID aggiunto
        for(Bean b: atleti) {
        	Atleta a = (Atleta) b;
        	if(a.getUsername().equals(nickpartecipante)) {
        		EmailPartecipante = a.getEmail();
        	}
        	
        }
		
		return EmailPartecipante;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
