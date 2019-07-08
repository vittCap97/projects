package storage;

/**
 * Rappresenta un campetto da calcio
 * @author Fernet
 *
 */
public class Campetto extends Bean{
	

	private static final long serialVersionUID = -2163079080317808697L;
	private String nome;
	private String tariffa;
	private String citta;
	private float valutazione;
	private boolean agibilita;
	
	
	
	public Campetto() {
		super();
		setNome("campetto");
		setTariffa("50 euro");
		setCitta("Citta");
		setValutazione(0);
		setAgibilita(false);		
	}

	

	public Campetto(String nome, String tariffa, String citta, float valutazione, boolean agibilita) {
		super();
		this.nome = nome;
		this.tariffa = tariffa;
		this.citta = citta;
		this.valutazione = valutazione;
		this.agibilita = agibilita;
	}



	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getTariffa() {
		return tariffa;
	}


	public void setTariffa(String string) {
		this.tariffa = string;
	}


	public String getCitta() {
		return citta;
	}


	public void setCitta(String citta) {
		this.citta = citta;
	}


	public float getValutazione() {
		return valutazione;
	}


	public void setValutazione(float valutazione) {
		this.valutazione = valutazione;
	}


	public boolean isAgibilita() {
		return agibilita;
	}


	public void setAgibilita(boolean agibilita) {
		this.agibilita = agibilita;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Campetto other = (Campetto) obj;
		if (agibilita != other.agibilita)
			return false;
		if (citta == null) {
			if (other.citta != null)
				return false;
		} else if (!citta.equals(other.citta))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tariffa == null) {
			if (other.tariffa != null)
				return false;
		} else if (!tariffa.equals(other.tariffa))
			return false;
		if (Float.floatToIntBits(valutazione) != Float.floatToIntBits(other.valutazione))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Campetto [nome=" + nome + ", tariffa=" + tariffa + ", citta=" + citta + ", valutazione=" + valutazione
				+ ", agibilita=" + agibilita + "]";
	}
	
	
	

	
	

}
