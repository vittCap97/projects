package Servlets;

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
import storage.StorageFacade;

/**
 * Servlet implementation class ValutaCampettoServlet
 */
@WebServlet("/ValutaCampettoServlet")
public class ValutaCampettoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;
 

    public ValutaCampettoServlet() {
        super();
		 storage = new StorageFacade();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String valutazione = request.getParameter("valutazione");
		String nomeCampetto = request.getParameter("campetto");

		System.out.println("Sono entrato"+valutazione);
		
	    PrintWriter out = response.getWriter();
		
		 Collection<Bean> campetti = (Collection<Bean>)storage.getLista("storage.Campetto");
		 for(Bean c : campetti) {
			 Campetto camp = (Campetto) c;
			 System.out.println(camp.getNome()+" "+nomeCampetto  );
			 if(camp.getNome().equals(nomeCampetto)) {
				 camp.setValutazione(Integer.parseInt(valutazione));
					storage.aggiorna(camp);
					out.println("operazione completata");
					return;

			 }
		 }
			out.println("operazione non riuscita");

		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
