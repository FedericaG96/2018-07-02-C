package it.polito.tdp.extflightdelays.model;

public class Rotta {
	
	private Airport source;
	private Airport destination;
	private double peso;
	public Rotta(Airport source, Airport destination) {
		super();
		this.source = source;
		this.destination = destination;
		
	}
	public Airport getSource() {
		return source;
	}
	public void setSource(Airport source) {
		this.source = source;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}

}
