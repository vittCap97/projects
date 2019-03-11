package storage;

public class Gioca extends Bean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String atleta_Email;
	private int partita_ID;
	private String statoInvito;
	
	
	public Gioca() {
		super();
		setAtleta_Email("atleta@email.it");
		setPartita_ID(1);
		setStatoInvito("Sicuro");
	}


	public String getAtleta_Email() {
		return atleta_Email;
	}


	public void setAtleta_Email(String atleta_Email) {
		this.atleta_Email = atleta_Email;
	}


	public int getPartita_ID() {
		return partita_ID;
	}


	public void setPartita_ID(int partita_ID) {
		this.partita_ID = partita_ID;
	}


	public String getStatoInvito() {
		return statoInvito;
	}


	public void setStatoInvito(String statoInvito) {
		this.statoInvito = statoInvito;
	}

}
