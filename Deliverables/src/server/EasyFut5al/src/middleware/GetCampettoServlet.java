package middleware;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.Bean;
import storage.Campetto;
import storage.Gestore;
import storage.StorageFacade;

/**
 * Servlet che restituisce il nome del campetto del gestore richiedente
 */
@WebServlet("/GetCampettoServlet")
public class GetCampettoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCampettoServlet() {
        super();
		storage = StorageFacade.getInstance();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String myEmail = request.getParameter("myEmail");
		
	    PrintWriter out = response.getWriter();
   
        Collection<Bean> gestori = storage.getLista("storage.Gestore");

        for(Bean g: gestori) {
        	Gestore ge = (Gestore) g;
        	if(ge.getEmail().equals(myEmail)) {
        		Campetto mioCampetto = (Campetto) storage.getOggetto("storage.Campetto", ge.getCampetto_ID());
        		out.println(mioCampetto.getNome());
        		return;
        	}
        	
        }

        
		out.println("Errore");

        
        
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
