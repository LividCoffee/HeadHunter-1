package com.neo.headhunter.util.item.sign;

import java.util.UUID;

public final class SellingSign extends HunterSign {
    public SellingSign(UUID owner) {
        super(owner, Type.SELLING);
    }
}
