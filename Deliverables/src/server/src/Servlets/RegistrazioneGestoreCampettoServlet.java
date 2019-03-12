package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import storage.Bean;
import storage.Campetto;
import storage.Gestore;
import storage.StorageFacade;

/**
 * Servlet implementation class RegistrazioneCampettoServlet
 */
@WebServlet("/RegistrazioneGestoreCampettoServlet")
public class RegistrazioneGestoreCampettoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
	private Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrazioneGestoreCampettoServlet() {
        super();
        gson = new Gson();
		storage = new StorageFacade();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Ricevo Campetto dal Client
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Messaggio ricevuto dal client  : " +message);		
        Campetto newCampetto = gson.fromJson(message, Campetto.class); //Creo un bean dal json del client
        System.out.println("Bean dal client(Campetto):"+ newCampetto.getNome());
        int checkCampetto = storage.Salva(newCampetto);
        
        //Ricevo Gestore dal Client
        message = br.readLine();
        System.out.println("Messaggio ricevuto dal client  : " +message);		
        Gestore newgestore = gson.fromJson(message, Gestore.class); //Creo un bean dal json del client
        System.out.println("Bean dal client(Gestore):"+ newgestore.getNome());
        
        Collection<Bean> campetti = storage.getLista("storage.Campetto");
        //MMMMMMH
		 newgestore.setCampetto_ID(campetti.size()); //Chiave esterna dell'ultimo campetto inserita
		 int checkGestore= storage.Salva(newgestore);
	 
		 
		 //se uno dei due fallisce
		 if(checkCampetto==1 || checkGestore==1) {
        	PrintWriter out = response.getWriter();
    	    out.println("Registrazione fallita");
        }
        else{
        	PrintWriter out = response.getWriter();
    	    out.println("Registrazione terminata con successo");
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