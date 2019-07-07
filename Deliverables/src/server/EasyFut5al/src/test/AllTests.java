package test;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests{
	
	public static Test suite() {
		
		TestSuite suite = new TestSuite();
		//Test StorageFacade
		suite.addTest(new StorageFacadeTest("GetOggetto_AtletaValido"));
		suite.addTest(new StorageFacadeTest("GetOggetto_GestoreNonValido"));
		suite.addTest(new StorageFacadeTest("Get_OggettoGiocaNonValido"));
		suite.addTest(new StorageFacadeTest("Get_Oggetto_CampettoValido"));
		suite.addTest(new StorageFacadeTest("Get_OggettoPartitaNonValido"));
		suite.addTest(new StorageFacadeTest("Get_OggettoNullNonValido"));
		suite.addTest(new StorageFacadeTest("GetLista_atleta"));
		suite.addTest(new StorageFacadeTest("GetLista_gestore"));
		suite.addTest(new StorageFacadeTest("GetLista_gioca"));
		suite.addTest(new StorageFacadeTest("GetLista_campetto"));
		suite.addTest(new StorageFacadeTest("GetLista_partita"));
		suite.addTest(new StorageFacadeTest("GetLista_ERR"));
		
		//Test GetInfoServlet
		suite.addTest(new GetInfoServletTest("InfoDoGet_Atleta"));
		suite.addTest(new GetInfoServletTest("InfoDoGet_Campetto"));
		suite.addTest(new GetInfoServletTest("InfoDoGet_ERR"));
		
		//Test CercaPartiteServlet
		suite.addTest(new CercaPartiteServletTest("Mia_DaGiocare_Valid"));
		suite.addTest(new CercaPartiteServletTest("InSos_NonValid"));
		suite.addTest(new CercaPartiteServletTest("Terminata_Valid"));
		suite.addTest(new CercaPartiteServletTest("Error_NonValid"));

		return suite;
	}
	
	
	public static void main() {
		junit.textui.TestRunner.run(suite());
	}

} 