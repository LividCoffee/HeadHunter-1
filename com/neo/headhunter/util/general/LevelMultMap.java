package com.neo.headhunter.util.general;

import com.neo.headhunter.util.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class LevelMultMap {
    private HashMap<Integer, Double> data;

    public LevelMultMap(String mapCode) {
        this.data = new HashMap<>();
        if(mapCode != null) {
            for (String pair : mapCode.split("/")) {
                String[] levelMult = pair.split(":", 2);
                if (levelMult.length == 2) {
                    if (Utils.isInteger(levelMult[0]) && Utils.isNumeric(levelMult[1]))
                        data.put(Integer.parseInt(levelMult[0]), Double.parseDouble(levelMult[1]));
                    else
                        Bukkit.getConsoleSender().sendMessage("§eInvalid level-multiplier code discovered: §c" + pair);
                } else
                    Bukkit.getConsoleSender().sendMessage("§eInvalid level-multiplier code discovered: §c" + pair);
            }
        }
    }

    public double getMultiplier(int level) {
        int maxLevel = -1;
        double maxMult = -1;
        for(Map.Entry<Integer, Double> entry : data.entrySet()) {
            if(level == entry.getKey())
                return entry.getValue();
            if((maxLevel == -1 && maxMult == -1) || maxLevel < entry.getKey()) {
                maxLevel = entry.getKey();
                maxMult = entry.getValue();
            }
        }
        return level > maxLevel ? maxMult : 1.0;
    }
}
