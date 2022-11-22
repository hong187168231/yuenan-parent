package com.indo.admin.modules.sys.controller;

import com.indo.admin.pojo.vo.RouteVO;
import com.indo.admin.modules.sys.service.ISysMenuService;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xianrui
 * @date 2021/6/1 22:40
 */
@Api(tags = "路由接口")
@RestController
@RequestMapping("/api/v1/routes")
@Slf4j
@AllArgsConstructor
public class RouteController {

    private ISysMenuService iSysMenuService;

    @ApiOperation(value = "路由列表")
    @GetMapping
    public Result list(HttpServletRequest request) {
        String countryCode = request.getHeader("countryCode");
        List<RouteVO> list = iSysMenuService.listRoute();
        if(StringUtils.isNotEmpty(countryCode)&&!countryCode.equals("CN")){
            list.forEach(l->{
                l.getMeta().setTitle(MessageUtils.get(l.getName(),countryCode));
                l.getChildren().forEach(ll->{
                    ll.getMeta().setTitle(MessageUtils.get(ll.getName(),countryCode));
                });
            });
        }
        return Result.success(list);
    }
}


