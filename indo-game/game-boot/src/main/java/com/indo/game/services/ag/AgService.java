package com.indo.game.services.ag;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

public interface AgService {

    /**
     * ag跳转
     *
     * @param actype
     * @param gameType
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    Result<String> agJump(LoginInfo loginUser, String actype, String gameType, String ip) throws ParserConfigurationException, SAXException, IOException, Exception;

    /**
     * 初始化游戏账号信息
     *
     * @param actype
     * @param gameType
     */
    void initAndStartGanmeAG(LoginInfo loginUser, String actype, String gameType, String ip);

    /**
     * 退出--下分
     */
    Result<String> exit(LoginInfo loginUser, String ip);


    /**
     * 定时拉取AG FTP xml
     */
    void pullAGFTPXml();

    /**
     * 定时拉取AG FTP YOPLAY xml
     */
    void pullAGYOPLAYXml();

    void pullNextAGYOPLAYXml();

    void pullAGAginXml();

    String commonInvoking(Integer uid, HashMap<Object, Object> hashMap, String apiURL, String ip, String type) throws Exception;

    void pullAGHunterXml();

    void pullAGNextHunterXml();
}
