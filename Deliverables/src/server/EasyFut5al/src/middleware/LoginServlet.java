package middleware;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import storage.Atleta;
import storage.Bean;
import storage.Campetto;
import storage.Gestore;
import storage.StorageFacade;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

@WebServlet("/LoginServlet")

public class LoginServlet extends HttpServlet {

	
	private static final long serialVersionUID = -4835978115742050945L;
	private StorageFacade storage;
	private boolean CampettoAgibile = false;
	private boolean IsAtleta;

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			           throws ServletException, java.io.IOException {
		
			System.out.println(request.getParameter("email"));
			System.out.println(request.getParameter("pwd"));

			Atleta user = new Atleta();
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("pwd"));
     
			    
			if (isValid(user)){
				
				HttpSession session = request.getSession(true);	    
				session.setAttribute("currentSessionUser",user); 
				System.out.println("Login effettuato!");
				
				if(IsAtleta) {
					 System.out.println("Sei un atleta!");
					request.getSession().setAttribute("userRoles", "atleta");
					PrintWriter out = response.getWriter();
					out.println("Lutente risulta autenticato come atleta");
					}
				
				else if(IsAtleta==false && CampettoAgibile==false) {
					 System.out.println("Gestore con campetto da verificare!");

					request.getSession().setAttribute("userRoles", "gestore");
					PrintWriter out = response.getWriter();
					out.println("Lutente risulta autenticato come gestore ma il campetto non ancora stato verificato");
				}
				
				else if(IsAtleta==false && CampettoAgibile==true) {
					 System.out.println("Gestore con campetto agibile!");
					request.getSession().setAttribute("userRoles", "gestore");
					PrintWriter out = response.getWriter();
					out.println("Lutente risulta autenticato come gestore");
				}
				
				


		        
			}
	        
			else {
				System.out.println("Errore login\n");
		        PrintWriter out = response.getWriter();
		        out.println("Errore Login");
			}
			
			
	}
	
	
	public boolean isValid(Atleta current_user) {
		
		storage = StorageFacade.getInstance();
		 Collection<Bean> atleti =  storage.getLista("storage.Atleta");
		 Collection<Bean> gestori =  storage.getLista("storage.Gestore");

	 
		 Iterator<Bean> iter = atleti.iterator();
		 		   
		 //Vedi se è un atleta già registrato
		 while (iter.hasNext()) {
			 Atleta unAtleta = (Atleta) iter.next();
			 //System.out.println(unAtleta.getEmail()+" "+unAtleta.getPassword());
			 if(current_user.getPassword().equals(unAtleta.getPassword())) 
			 if(unAtleta.getEmail().equals(current_user.getEmail()) && 
					 unAtleta.getPassword().equals(current_user.getPassword())) {
					 System.out.println("Password e email sono uguali!");
					 IsAtleta = true;
					 
					 return true;
			  }
		}
		 
		 
		 Iterator<Bean> iter2 = gestori.iterator();

		//Vedi se è un gestore già registrato
		 while (iter2.hasNext()) {
			 Gestore unGestore = (Gestore) iter2.next();
			 System.out.println(unGestore.getEmail()+" "+unGestore.getPassword()+" Campetto_ID:"+unGestore.getCampetto_ID());
			 
			 if(current_user.getPassword().equals(unGestore.getPassword())) 
				 
			 if(unGestore.getEmail().equals(current_user.getEmail()) && 
					 unGestore.getPassword().equals(current_user.getPassword())) {
					 System.out.println("Password e email sono uguali!");
					 IsAtleta = false;
					 
					 Collection<Bean> campetti =  storage.getLista("storage.Campetto");
					 System.out.println("Numero campetti trovati:"+campetti.size());
					 Iterator<Bean> iter3 = campetti.iterator();
					 while(iter3.hasNext()) {
						 Campetto uncampetto = (Campetto) iter3.next();

						 System.out.println(unGestore.getID()+" "+uncampetto.getID());
						 if(uncampetto.getID() == unGestore.getCampetto_ID()) {
							 if(uncampetto.isAgibilita()) CampettoAgibile = true;
							 
						 }
						 
					 }
					 
					 return true;
			  }
		}
		 
	
		return false;
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
}