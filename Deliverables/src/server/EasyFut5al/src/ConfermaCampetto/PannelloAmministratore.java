package ConfermaCampetto;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

import javax.swing.*;

import storage.Bean;
import storage.Campetto;
import storage.StorageFacade;

/**
 * GUI che usa l'amministratore per confermare i campetti validi
 * @author Fernet
 *
 */
public class PannelloAmministratore {
	
	private static StorageFacade storage;
	private static int numCampetti=0;

	
  public static void main(String[] args) {
   
	  
	JFrame f = new JFrame("Pannello Amministratore");
    f.setSize(500, 200);
    f.setLocation(200, 200);
   
    f.addWindowListener(new WindowAdapter( ) {
      public void windowClosing(WindowEvent we) { System.exit(0); }
    });
    
    
    Container content = f.getContentPane( );
    JLabel titolo = new JLabel("Campetti in attesa di conferma:");
    content.add(titolo);


    //Trova campetti
	storage = StorageFacade.getInstance();
    Collection<Bean> campetti = (Collection<Bean>)storage.getLista("storage.Campetto");
	 for(Bean c : campetti) {
		 Campetto camp = (Campetto) c;
		 
		 if(camp.isAgibilita()==false) {
		    JPanel PanelRecord = new JPanel( );
		    PanelRecord.add(new JLabel(String.valueOf(camp.getID())));
		    PanelRecord.add(new JLabel(camp.getNome()));
		    PanelRecord.add(new JLabel(camp.getCitta()));
		    PanelRecord.add(new JLabel(camp.getTariffa()));
		    //PanelRecord.add(new JLabel(camp.isAgibilita()));

		    //Button agibile
		    JButton agibilebutton = new JButton("Rendi agibile");
		    PanelRecord.add(agibilebutton);
		    agibilebutton.addActionListener(new ActionListener()
		    {
		    	  public void actionPerformed(ActionEvent e)
		    	  {
			    	camp.setAgibilita(true);
			    	storage.aggiorna(camp);
			    	PanelRecord.setBackground(Color.GREEN);
		    	  }
		    	});
		    
		    
		    //non agibile
		    JButton nonagibileButton = new JButton("Rifiuta");
		    PanelRecord.add(nonagibileButton);
		    nonagibileButton.addActionListener(new ActionListener()
		    {
		    	  public void actionPerformed(ActionEvent e)
		    	  {
				    	camp.setAgibilita(false);
				    	storage.aggiorna(camp);
				    	PanelRecord.setBackground(Color.RED);

		    	  }
		    	});
		 

		
		    content.add(PanelRecord);
		 }
	 }
	 content.setLayout(new GridLayout(numCampetti, 1));

	    JButton riavvia = new JButton("Riavvia");
	    riavvia.addActionListener(new ActionListener()
	    {
	    	  public void actionPerformed(ActionEvent e)
	    	  {
	    		  f.setVisible(false);
	    		  String[] args = new String[0]; 
	    		  main(args);
	    	  }
	    	});
	    content.add(riavvia);

    
    f.setVisible(true);
  }
  

  
  
  
  
}