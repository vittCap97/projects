package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


import storage.Atleta;
import storage.StorageFacade;


/**
 * Servlet implementation class RegistrazioneAtletaServlet
 */
@WebServlet("/RegistrazioneAtletaServlet")
public class RegistrazioneAtletaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
	private Gson gson;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrazioneAtletaServlet() {
        super();
        gson = new Gson();
		storage = new StorageFacade();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//Ricevo un json dal client
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String message = br.readLine();
        System.out.println("Messaggio ricevuto dal client  : " +message);		
        Atleta newAtleta = gson.fromJson(message, Atleta.class); //Creo un bean dal json del client
        System.out.println("Bean dal client:"+ newAtleta.getCognome());
        //salva l'oggetto atleta
        
        
        if(storage.Salva(newAtleta)==1) {
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
		doGet(request, response);
	}

}
