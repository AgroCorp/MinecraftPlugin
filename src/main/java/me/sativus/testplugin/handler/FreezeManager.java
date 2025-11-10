package me.sativus.testplugin.handler;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FreezeManager {
    private static FreezeManager instance;
    private final Set<UUID> frozenPlayers = new HashSet<>();


    private FreezeManager() {
    }

    public void setFrozen(UUID playerId, boolean frozen) {
        if (frozen) {
            frozenPlayers.add(playerId);
        } else {
            frozenPlayers.remove(playerId);
        }
    }

    public boolean contains(UUID playerId) {
        return frozenPlayers.contains(playerId);
    }

    public static FreezeManager getInstance() {
        if (instance == null) {
            instance = new FreezeManager();
        }
        return instance;
    }
}
