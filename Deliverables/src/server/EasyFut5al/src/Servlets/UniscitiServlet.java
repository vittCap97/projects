package Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.Gioca;
import storage.StorageFacade;

/**
 * Servlet implementation class UniscitiServlet
 */
@WebServlet("/UniscitiServlet")
public class UniscitiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UniscitiServlet() {
        super();
		storage = new StorageFacade();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();

		System.out.println("Salve nuovo amico!");
        InputStream stream = request.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));        
        String Atleta =  buffer.readLine();
        String partitaScelta = buffer.readLine();
		System.out.println(Atleta+ " vuole partecipare alla partita con codice "+partitaScelta);
		
		//Crea una partecipazione alla partita come sicuro..
        //Inizio a far partecipare l'atleta organizzatore
        Gioca g1 = new Gioca();
        g1.setAtleta_Email(Atleta);
        g1.setStatoInvito("Sicuro");
        g1.setPartita_ID(Integer.parseInt(partitaScelta)); 
        int check = storage.Salva(g1);
        if(check == 0) out.println("Operazione terminata con successo!");
        else out.println("Impossibile completare l'operazione!");

        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
