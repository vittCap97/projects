package test;

import storage.StorageFacade;

public class testStorage {

	public testStorage() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		StorageFacade store = StorageFacade.getInstance();
		
		System.out.println(store.IDultimaPartita());
		

	}

}
