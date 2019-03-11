package PrenotazioniManager;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PrenotazioneManagement implements ServletContextListener {

    private ScheduledExecutorService scheduler;
;


    @Override
    public void contextInitialized(ServletContextEvent event) {
    	
		System.out.println("Avviamento del Server..");
    	ServletContext ctx = event.getServletContext();
    	String partitaTrovata = ctx.getInitParameter("PartitaTrovata");

        //scheduler = Executors.newSingleThreadScheduledExecutor();
        
        //trova partite "da giocare" 1 volta al secondo
        //scheduler.scheduleAtFixedRate(new checkpartiteDagiocare(), 0, 1, TimeUnit.SECONDS);
   


    }
    
  
    
 
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

}