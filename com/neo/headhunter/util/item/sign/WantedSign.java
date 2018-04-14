package com.neo.headhunter.util.item.sign;

import org.bukkit.Location;

public final class WantedSign extends HunterSign {
    private int bountyIndex;
    private Location headLocation;

    public WantedSign() {
        super(Type.WANTED);
        this.bountyIndex = 0;
        this.headLocation = null;
    }

    public int getBountyIndex() {
        return bountyIndex;
    }

    public void setBountyIndex(int bountyIndex) {
        this.bountyIndex = Math.max(0, bountyIndex);
    }

    public Location getHeadLocation() {
        return headLocation;
    }

    public void setHeadLocation(Location headLocation) {
        this.headLocation = headLocation;
    }
}
