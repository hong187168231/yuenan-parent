package com.indo.admin.modules.chongzhika.controller;

import com.indo.admin.modules.chongzhika.service.ICardInfoService;
import com.indo.admin.modules.chongzhika.service.IExportService;
import com.indo.admin.pojo.req.chongzhika.CardInfoReq;
import com.indo.common.result.Result;
import com.indo.common.web.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private ICardInfoService iCardInfoService;
    @Autowired
    private IExportService iExportService;

    /**
     * 批量生成卡密
     * @param cardInfoReq
     * @return
     */
    @ApiOperation(value = "批量生成卡密", httpMethod = "POST")
    @PostMapping(value = "/addCardInfo")
    public Result addCardInfo(HttpServletRequest request, CardInfoReq cardInfoReq){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        CardInfoReq cardInfoReq1 = new CardInfoReq();
//        cardInfoReq1.setSerialLength(0);//序号长度
//        cardInfoReq1.setTotal(100000);//生成条数
//        String[] pstr = {"1"};
//        cardInfoReq1.setPwdLetterNumber(pstr);//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
//        cardInfoReq1.setPwdLength(6);//密码长度
//        String[] lstr = {"1"};
//        cardInfoReq1.setLetterNumber(lstr);//卡号生成规则   0：字母 1：数字（字母数字都传表示数字与字母组合）
//        cardInfoReq1.setLetterNumberLength(6);//卡号随机数长度
//        User user = UserContext.getCurrebtUser();
//        if(null!=user){
//            cardInfoReq.setUserId(user.getId());//登录ID
//        }else {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("用户登录已失效请重新登录");
//            return result;
//        }
//        if(null==cardInfoReq.getUserId()||"".equals(cardInfoReq.getUserId())){
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg(MessageUtils.get("a100001",countryCode));
//            return result;
//        }
        cardInfoReq.setUserId(JwtUtils.getUserId());//登录ID

//        cardInfoReq1.setCardNoPrefix("z");//前缀
//        cardInfoReq1.setCardAmount(10D);//卡面金额
//        cardInfoReq1.setAdditionalAmount(0D);//增送金额
//        cardInfoReq1.setUserId(1L);//登录ID
//        return iCardInfoService.insertCardInfoBatch(cardInfoReq1);
        return iCardInfoService.insertCardInfoBatch(cardInfoReq,countryCode);
    }

    /**
     * 查询激活卡片，语音提醒
     * @return
     */
    @ApiOperation(value = "查询激活卡片，语音提醒", httpMethod = "GET")
    @GetMapping(value = "/queryCardActi")
    public Result queryCardInfo(HttpServletRequest request){
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return iCardInfoService.selectCardInfoByisActivation(countryCode);
    }

    /**
     * 查询所有前缀
     * @return
     */
    @ApiOperation(value = "查询所有前缀", httpMethod = "GET")
    @GetMapping(value = "/queryCardNoPrefix")
    public Result queryCardNoPrefix(HttpServletRequest request){
//        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
        return iCardInfoService.selectCardNoPrefix(countryCode);
    }

    /**
     * 导出卡号信息
     * @param response
     * @param cardInfoReq
     * @return
     */
    @ApiOperation(value = "导出卡号信息", httpMethod = "POST")
    @RequestMapping(value = "/download",method = RequestMethod.POST)
    @ResponseBody
    public Result cardInfoDownload(HttpServletRequest request,HttpServletResponse response,CardInfoReq cardInfoReq){
        //        Header头带参，"countryCode":"VN" 越南 "IN" 印度 "CN"中国 "EN"英语
        String countryCode = request.getHeader("countryCode");
//        User user = UserContext.getCurrebtUser();
//        if(null==user) {
//            Result result = new Result();
//            result.setSuccess(false);
//            result.setMsg("用户登录已失效请重新登录");
//            return result;
//        }
        return iExportService.exportCardInfo(response,cardInfoReq.getCardNoPrefix(),countryCode);
    }

    @PostMapping(value = "/myTest")
    public String  myTest(CardInfoReq cardInfoReq){
        System.out.println("print Hello word");
        return "Hello word";
    }
}
