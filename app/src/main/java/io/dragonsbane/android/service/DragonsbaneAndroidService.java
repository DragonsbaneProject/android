package io.dragonsbane.android.service;

import java.util.Properties;

import io.onemfive.android.api.service.OneMFiveAndroidRouterService;

public class DragonsbaneAndroidService extends OneMFiveAndroidRouterService {

    public DragonsbaneAndroidService() {
        super(DragonsbaneAndroidService.class.getName());
        Properties p = new Properties();
        p.put(OneMFiveAndroidRouterService.PARAM_ROOT_DIR,"dgb");
        super.properties = p;
    }
}
