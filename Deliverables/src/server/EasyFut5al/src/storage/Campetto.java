package storage;

public class Campetto extends Bean{
	
	/**
	 * 
	 */
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
	

	
	

}
