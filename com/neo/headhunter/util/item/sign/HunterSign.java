package com.neo.headhunter.util.item.sign;

import java.util.UUID;

public abstract class HunterSign {
	private final UUID owner;
	private final Type type;
	
	HunterSign(UUID owner, Type type) {
		this.owner = owner;
		this.type = type;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		SELLING, WANTED
	}
}
