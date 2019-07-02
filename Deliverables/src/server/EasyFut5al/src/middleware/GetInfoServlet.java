package middleware;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import storage.Atleta;
import storage.Bean;
import storage.Campetto;

import storage.StorageFacade;

/**
 * Servlet implementation class CercaPartiteServlet
 */
@WebServlet("/GetInfoServlet")
public class GetInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
	private String tipo_oggetto;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInfoServlet() {
		storage = StorageFacade.getInstance();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
		System.out.println("voglio una lista di..."+request.getParameter("tipo_oggetto"));
		tipo_oggetto = request.getParameter("tipo_oggetto");
		
	    PrintWriter out = response.getWriter();
   
         Collection<Bean> beans = storage.getLista("storage."+tipo_oggetto);
		 for(Bean b: beans) {
			 
			 if(tipo_oggetto.equals("Atleta")) { 
				 Atleta a = (Atleta) b;
				 out.println(a.getUsername());}
			 
			 else if(tipo_oggetto.equals("Campetto")) {
				 Campetto c = (Campetto) b;
				 if(c.isAgibilita()) out.println(c.getNome()); // solo quelli agibili però
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

