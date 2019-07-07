package test;


import java.util.Collection;

import org.junit.jupiter.api.Test;


import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;

import junit.framework.TestCase;
import middleware.GetInfoServlet;
import storage.Atleta;
import storage.Bean;
import storage.StorageFacade;
import storage.Campetto;

public class GetInfoServletTest extends TestCase{
	
	private ServletTestModule tester;
	private WebMockObjectFactory factory;
	private StorageFacade storage;




	 public GetInfoServletTest(String name){
		 super(name);
	}
	 
	 @Override
	protected void setUp() throws Exception {	
			
			factory = new WebMockObjectFactory();
			tester = new ServletTestModule(factory);
			storage = StorageFacade.getInstance();
	 }
	 
	 
	
	@Test
	public void InfoDoGet_Atleta(){
	
		String tipo = "Atleta";
		
		tester.addRequestParameter("tipo_oggetto", tipo);
		tester.createServlet(GetInfoServlet.class);
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		
		String testo[] = output.split("\\r?\\n");
		
		Collection<Bean> atleti =  storage.getLista("storage.Atleta");
		int i = 0;
		for(Bean b: atleti) {
			Atleta a = (Atleta) b;
			assertEquals(testo[i], a.getUsername());
			i++;
		}	
	}
	
	
	
	
	@Test
	public void InfoDoGet_Campetto(){
		
		String tipo = "Campetto";
		tester = new ServletTestModule(factory);
		tester.addRequestParameter("tipo_oggetto", tipo);

		tester.createServlet(GetInfoServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		
		String testo[] = output.split("\\r?\\n");
		
		Collection<Bean> atleti =  storage.getLista("storage.Campetto");
		int i = 0;
		for(Bean b: atleti) {
			Campetto a = (Campetto) b;
			if(a.isAgibilita())
				assertEquals(testo[i], a.getNome());
			i++;
		}	
	}
	
	
	@Test
	public void InfoDoGet_ERR() {
		
		String tipo = "Albicocca";
		tester = new ServletTestModule(factory);
		tester.addRequestParameter("tipo_oggetto", tipo);

		tester.createServlet(GetInfoServlet.class);
		
		tester.doGet();
		
		String output = factory.getMockResponse().getOutputStreamContent();
		
		assertTrue(output.length()==0);
	}
	
		
	

}
