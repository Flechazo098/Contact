package com.flechazo.contact.platform.fabric;

import com.flechazo.contact.platform.IPlatformService;

public class PlatformHelperImpl {
    public static IPlatformService getPlatformService() {
        return new FabricPlatformService();
    }
}
