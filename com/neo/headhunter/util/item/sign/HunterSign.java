package com.neo.headhunter.util.item.sign;

public abstract class HunterSign {
	private final Type type;
	
	HunterSign(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		SELLING, WANTED
	}
}
