package fr.galize.desktopsms.util;

public class Couple<T,U> {
	
	T a;
	U b;
	public T getA() {
		return a;
	}
	public void setA(T a) {
		this.a = a;
	}
	public U getB() {
		return b;
	}
	public void setB(U b) {
		this.b = b;
	}
	public Couple(T a, U b) {
		super();
		this.a = a;
		this.b = b;
	}
	public Couple() {
		super();
	}

}
