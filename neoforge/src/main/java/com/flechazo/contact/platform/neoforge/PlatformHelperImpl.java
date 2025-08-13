package com.flechazo.contact.platform.neoforge;

import com.flechazo.contact.platform.IPlatformService;

public class PlatformHelperImpl {
    public static IPlatformService getPlatformService() {
        return new NeoForgePlatformService();
    }
}
