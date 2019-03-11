package storage;

public class Atleta extends Bean {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3181493217886046261L;
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private String data;
	private String residenza;
	private String ruolo;
	private String username;
	

	public Atleta() {
		super();
		setNome("Nome");
		setCognome("Cognome");
		setData("1900-12-12");
		setEmail("email@email.it");
		setPassword("password");
		setResidenza("residenza");
		setRuolo("ruolo");
		setUsername("username");
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



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}



	public String getResidenza() {
		return residenza;
	}



	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}



	public String getRuolo() {
		return ruolo;
	}



	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}




}