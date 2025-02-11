package org.rammex.factionflymodule.util;

import org.rammex.factionflymodule.FactionFlyModule;

public class MessagesConfigManager {
    public static String getMessage(String key) {
        return FactionFlyModule.getInstance().getConfig().getString(key);
    }
}
