package middleware;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import storage.Atleta;
import storage.Bean;
import storage.Campetto;
import storage.Gestore;
import storage.StorageFacade;

/**
 * Servlet implementation class GetProfiloServlet
 */
@WebServlet("/GetProfiloServlet")
public class GetProfiloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private boolean ModAtleta = false;
	private StorageFacade storage;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProfiloServlet() {
        super();
		storage = StorageFacade.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String emailutente = request.getParameter("MyEmail");
		String IamAtleta = request.getParameter("IamAtleta");
	    PrintWriter out = response.getWriter();

		
		if(IamAtleta.equals("Si")) ModAtleta = true;
		
		if(ModAtleta) {
			Collection<Bean> atleti = storage.getLista("storage.Atleta");
			for(Bean b: atleti) {
				Atleta a = (Atleta) b;
				if(a.getEmail().equals(emailutente)) {
			         
					JsonObject obj = new JsonObject();
			         obj.addProperty("Nome", a.getNome());
			         obj.addProperty("Cognome", a.getCognome());
			         obj.addProperty("Username", a.getUsername());
			         obj.addProperty("Email", a.getEmail());
			         obj.addProperty("Password", a.getPassword());
			        
			         obj.addProperty("DataDiNascita", a.getData());
			         obj.addProperty("Residenza", a.getResidenza());
			         obj.addProperty("Ruolo", a.getRuolo());
   
			         String ItemDaInviare = obj.toString();
			         out.println(ItemDaInviare);
			         return;
				}
			}
			

		}
		else {
			System.out.print("Gestoree");
			Collection<Bean> gestori = storage.getLista("storage.Gestore");
			for(Bean b: gestori) {
				Gestore a = (Gestore) b;
				if(a.getEmail().equals(emailutente)) {
			         
					JsonObject obj = new JsonObject();
			         obj.addProperty("Nome", a.getNome());
			         obj.addProperty("Cognome", a.getCognome());
			         obj.addProperty("Username", a.getUsername());
			         obj.addProperty("Email", a.getEmail());
			         obj.addProperty("Password", a.getPassword());
			        
			         Campetto c = (Campetto )storage.getOggetto("storage.Campetto", a.getCampetto_ID());
			         obj.addProperty("NomeCampetto", c.getNome());
			         obj.addProperty("Tariffa", c.getTariffa());
			         obj.addProperty("Valutazione", c.getValutazione() );
   
			         String ItemDaInviare = obj.toString();
			         out.println(ItemDaInviare);
			         System.out.println("gestore"+ItemDaInviare);
			         return;
				}
			}
		}
		
		
        out.println("Errore!");


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
