package com.indo.game.service.pp;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.GameUtil;
import com.indo.game.common.util.PPHashAESEncrypt;
import com.indo.game.pojo.dto.pp.PpApiRequestData;
import com.indo.game.pojo.dto.pp.PpApiStartGameReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PPTest {

    @Autowired
    PpService ppService;

    private String MERCHANT_CODE = "7a13c3a979734a649f1af49fa6a5f0f6";
    private String AGENT = "ZF168testag";
    private String DOMAIN = "zf01";
    private String API_URL = "https://api.prerelease-env.biz/IntegrationService/v3/http/CasinoGameAPI";

    @Test
    public void createPPTestPlayer() throws Exception {
        PpApiRequestData ppApiRequestData = new PpApiRequestData();
        ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
        ppApiRequestData.setExternalPlayerId("test_player");
        ppApiRequestData.setCurrency("USD");
        Map<String, Object> params = objectToMap(ppApiRequestData);

        System.out.println(
                GameUtil.postForm4PP(API_URL + "/player/account/create", params, "createPP"));

    }

    @Test
    public void testStartGame() throws Exception {
        PpApiStartGameReq ppApiRequestData = new PpApiStartGameReq();
        ppApiRequestData.setSecureLogin(OpenAPIProperties.PP_SECURE_LOGIN);
        ppApiRequestData.setExternalPlayerId("test_player");
        ppApiRequestData.setGameId("PP");
        ppApiRequestData.setLanguage("zh");
        Map<String, Object> params = objectToMap(ppApiRequestData);

        System.out.println(
                GameUtil.postForm4PP(API_URL + "/game/start", params, "startGame"));

    }

    @Test
    public void loginoutTestPlayer() throws Exception {
        String playerId = "playerId001";
        Map<String, String> param = new HashMap<>();
        param.put("playerID", playerId);
        param.put("actionType", "0");
        param.put("checkValue", getCheckValue(MERCHANT_CODE, AGENT, "0"));

        Map<String, String> header = new HashMap<>();
        header.put("agent", AGENT);
        header.put("domain", DOMAIN);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Accept-Encoding", "gzip");

//        System.out.println(GameUtil.doPostHeaderJson(getLogOutT9PlayerUrl(), param, header, 1, "loginoutPlayer"));

    }

    /**
     * 对象转MAP
     *
     * @param obj
     * @return
     */
    private Map<String, Object> objectToMap(Object obj) {
        String json = JSONObject.toJSONString(obj);
        Map<String, Object> params = JSONObject.parseObject(json);
        params.put("hash", PPHashAESEncrypt.encrypt(obj, OpenAPIProperties.PP_API_SECRET_KEY));
        return params;
    }

    private String getCheckValue(String... values) throws UnsupportedEncodingException {
        StringBuilder checkValue = new StringBuilder();
        for (String value : values) {
            checkValue.append(value);
        }
        return DigestUtils.md5DigestAsHex(checkValue.toString().getBytes("utf-8"));
    }


    /**
     * 玩家退出游戏API地址
     *
     * @return
     */
    private String getLogOutT9PlayerUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(API_URL).append("/party/kickplayer");
        return builder.toString();
    }

    private String gzipUnZiper(byte[] byteArray) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(byteArray));
        BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));
        StringWriter writer = new StringWriter();
        String line;
        while ((line = reader.readLine()) != null) {
            writer.write(line);
        }
        return writer.toString();
    }
}
