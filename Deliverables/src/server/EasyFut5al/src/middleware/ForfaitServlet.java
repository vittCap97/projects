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
import storage.Gioca;
import storage.StorageFacade;

/**
 * Servlet implementation class ForfaitServlet
 */
@WebServlet("/ForfaitServlet")
public class ForfaitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StorageFacade storage;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForfaitServlet() {
        super();
		storage = StorageFacade.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println( request.getParameter("MyEmail")+ "vuole rimuoversi dalla partita "+ request.getParameter("partita"));
		

		 Collection<Bean> giochi= (Collection<Bean>)storage.getLista("storage.Gioca");
		 for(Bean b: giochi) {
			 Gioca g = (Gioca) b;
			 if(g.getPartita_ID() ==Integer.parseInt(request.getParameter("partita")) && g.getAtleta_Email().equals(request.getParameter("MyEmail"))){
					storage.elimina(g);
				    PrintWriter out = response.getWriter();
				    out.println("Rimozione completata");
				    break;
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
