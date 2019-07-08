package storage;

/**
 * Rappresenta un Atleta
 * @author Fernet
 *
 */
public class Atleta extends Bean {
	

	private static final long serialVersionUID = 3181493217886046261L;
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private String data;
	private String residenza;
	private String ruolo;
	private String username;
	

	public Atleta(String email, String password, String nome, String cognome, String data, String residenza,
			String ruolo, String username) {
		super();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.data = data;
		this.residenza = residenza;
		this.ruolo = ruolo;
		this.username = username;
	}



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

	
	

	@Override
	public String toString() {
		return "Atleta [email=" + email + ", password=" + password + ", nome=" + nome + ", cognome=" + cognome
				+ ", data=" + data + ", residenza=" + residenza + ", ruolo=" + ruolo + ", username=" + username + "]";
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
//Ho tolto il super.equals del bean   perchè giustamente ID è automatico....
		if (getClass() != obj.getClass())
			return false;
		Atleta other = (Atleta) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		if (residenza == null) {
			if (other.residenza != null)
				return false;
		} else if (!residenza.equals(other.residenza))
			return false;
		if (ruolo == null) {
			if (other.ruolo != null)
				return false;
		} else if (!ruolo.equals(other.ruolo))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
	



}