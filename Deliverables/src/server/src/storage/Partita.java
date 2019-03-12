package storage;

public class Partita extends Bean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Data;
	private int Ora;
	private String StatoPartita;
	private int campetto_ID;

	
	
	public Partita(){
		super();
		setData("1900-12-12");
		setOra(1);
		setStatoPartita("daGiocare");
	}



	public String getData() {
		return Data;
	}



	public void setData(String data) {
		Data = data;
	}



	public int getOra() {
		return Ora;
	}



	public void setOra(int i) {
		Ora = i;
	}



	public String getStatoPartita() {
		return StatoPartita;
	}



	public void setStatoPartita(String statoPartita) {
		StatoPartita = statoPartita;
	}
	
	public int getCampetto_ID() {
		return campetto_ID;
	}

	public void setCampetto_ID(int campetto_ID) {
		this.campetto_ID = campetto_ID;
	}
}
