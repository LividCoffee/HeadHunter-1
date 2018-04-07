package com.neo.headhunter.util.general;

public final class Duplet<T, U> {
	private T t;
	private U u;
	
	public Duplet(T t, U u) {
		this.t = t;
		this.u = u;
	}
	
	public void setT(T t) {
		this.t = t;
	}
	
	public T getT() {
		return t;
	}
	
	public void setU(U u) {
		this.u = u;
	}
	
	public U getU() {
		return u;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Duplet) {
			Duplet o2 = (Duplet) obj;
			return t.equals(o2.t) && u.equals(o2.u);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return t.hashCode() + u.hashCode();
	}
}
