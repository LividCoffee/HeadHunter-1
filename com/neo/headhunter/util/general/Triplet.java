package com.neo.headhunter.util.general;

public final class Triplet<T, U, V> {
	private T t;
	private U u;
	private V v;
	
	public Triplet(T t, U u, V v) {
		this.t = t;
		this.u = u;
		this.v = v;
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
	
	public void setV(V v) {
		this.v = v;
	}
	
	public V getV() {
		return v;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Triplet) {
			Triplet o2 = (Triplet) obj;
			return t.equals(o2.t) && u.equals(o2.u) && v.equals(o2.v);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return t.hashCode() + u.hashCode() + v.hashCode();
	}
}
