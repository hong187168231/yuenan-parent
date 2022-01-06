package com.indo.admin.modules.game.service;

import com.indo.admin.pojo.dto.game.sbo.SboAgentDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentPresetBetDTO;
import com.indo.admin.pojo.dto.game.sbo.SboUpdateAgentStatusDTO;
import com.indo.admin.pojo.vo.game.sbo.SboApiResponseData;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

import java.util.Map;

public interface SboService {

    public Result registerAgent(SboAgentDTO sboAgentDTO, String ip);

    public Result updateAgentStatus(SboUpdateAgentStatusDTO sboUpdateAgentStatusDTO, String ip);

    public Result updateAgentPresetBet(SboUpdateAgentPresetBetDTO sboUpdateAgentPresetBetDTO, String ip);

}
