package com.indo.admin.modules.game.service;

import com.indo.admin.pojo.dto.game.sbo.SboAgentDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentPresetBetDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentStatusDTO;
import com.indo.admin.pojo.vo.game.sbo.SboApiResponseData;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

import java.util.Map;

public interface SboService {

    public Result registerAgent(SboAgentDTO sboAgentDTO, LoginInfo loginUser, String ip);

    public Result updateAgentStatus(SboUpdateAgentStatusDTO sboUpdateAgentStatusDTO, LoginInfo loginUser, String ip);

    public Result updateAgentPresetBet(SboUpdateAgentPresetBetDTO sboUpdateAgentPresetBetDTO, LoginInfo loginUser, String ip);

    SboApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception;

}
