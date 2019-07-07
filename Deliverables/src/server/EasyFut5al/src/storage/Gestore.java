package storage;

public class Gestore extends Bean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2325798472262336420L;
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
	
	

	public Gestore(String email, String password, String nome, String cognome, String username, int campetto_ID) {
		super();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.campetto_ID = campetto_ID;
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




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Gestore other = (Gestore) obj;
		if (campetto_ID != other.campetto_ID)
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	



}
