package com.indo.admin.modules.chongzhika.service.impl;

import com.indo.common.result.Result;
import com.indo.core.mapper.chongzhika.CardInfoMapper;
import com.indo.admin.modules.chongzhika.service.IExportService;
import com.indo.core.pojo.entity.chongzhika.CardInfo;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.i18n.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ExportServiceImpl implements IExportService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CardInfoMapper cardInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result exportCardInfo(HttpServletResponse response, String cardNoPrefix,String countryCode){
        if(null==cardNoPrefix||"".equals(cardNoPrefix)){
            return Result.failed(MessageUtils.get("a100025",countryCode));
        }
        //用户主目录
        String userHome = System.getProperties().getProperty("user.home");
        logger.info("用户主目录,userHome:{}", userHome);
        String javaPath = System.getProperties().getProperty("java.home");
        logger.info("java实时运行环境安装目录,userHome:{}", javaPath);
        String osName = System.getProperties().getProperty("os.home");
        logger.info("操作系统名称,userHome:{}", osName);
        String userDir = System.getProperties().getProperty("user.dir");
        logger.info("当前用户程序所在目录,userHome:{}", userDir);
        //导出txt文件
//        response.setContentType("text/plain;charset=UTF-8");

        String fileName = cardNoPrefix + DateUtils.getDateTime(DateUtils.longFormat1)+".txt";
//        try {
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            result.failed("导出文件失败");
//        }
//        response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");
//        BufferedOutputStream buff = null;
        BufferedWriter out = null;
        StringBuffer write = new StringBuffer();
        String enter = "\r\n";
        List<String> fineNameList = new ArrayList<>();
        fineNameList.add(fileName);
//        ServletOutputStream outSTr = null;
        try {
//            outSTr = response.getOutputStream(); // 建立
//            buff = new BufferedOutputStream(outSTr);
            out = new BufferedWriter(new FileWriter(userDir+"/templates/"+fileName));
            List<CardInfo> list = cardInfoMapper.selectCardInfoByCardNoPrefix(cardNoPrefix);
            //把内容写入文件
            if(null!=list&&list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    CardInfo cardInfo = list.get(i);
                    write.append(cardInfo.getCardNo()+",");
                    write.append(cardInfo.getCardPwd());
                    write.append(enter);
                }
            }else {
                return Result.failed(MessageUtils.get("a100026",countryCode));
            }
            out.write(write.toString());

            cardInfoMapper.updateCardInfoByCardNoPrefix(cardNoPrefix, DateUtils.getDateTime(DateUtils.newFormat));
//            result.failed(MessageUtils.get("a100027",countryCode));
            return Result.success(fineNameList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(MessageUtils.get("a100028",countryCode));

        }
        finally {
            try {
                if(null!=out) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
