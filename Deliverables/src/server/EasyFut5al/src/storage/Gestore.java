package storage;

public class Gestore extends Bean{
	
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private String username;
	private  int campetto_ID;
	
	

	public Gestore() {
		super();
		setNome("Nome");
		setCognome("Cognome");
		setEmail("email@email.it");
		setPassword("password");
		setUsername("username");
		setCampetto_ID(0);
		
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public String getCognome() {
		return cognome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}







	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public int getCampetto_ID() {
		return campetto_ID;
	}



	public void setCampetto_ID(int campetto_ID) {
		this.campetto_ID = campetto_ID;
	}
	



}
