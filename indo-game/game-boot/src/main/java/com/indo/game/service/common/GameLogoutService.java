package com.indo.game.service.common;

import com.indo.common.result.Result;

public interface GameLogoutService {

    public Result logout(String account, String ip, String parentName, String countryCode);

    public Result gamelogout(String account, String ip, String countryCode);
}
