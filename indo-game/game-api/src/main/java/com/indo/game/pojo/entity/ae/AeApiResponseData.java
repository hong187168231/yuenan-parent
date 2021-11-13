package com.indo.game.pojo.entity.ae;

import java.util.HashMap;
import java.util.Map;

public class AeApiResponseData<T> {
    private Integer code;
    private String msg;
    private T result;

    private static Map<Integer, String> MSG_MAP;

    static {
        MSG_MAP = new HashMap<>();
        MSG_MAP.put(0, "成功");
        MSG_MAP.put(1, "失败");
        MSG_MAP.put(11, "超过上限");
        MSG_MAP.put(12, "余额不足");
        MSG_MAP.put(13, "在地图内,无法执行操作");
        MSG_MAP.put(14, "在游戏中,无法执行操作");
        MSG_MAP.put(99, "服务端返回数据超时（上分）");
        MSG_MAP.put(101, "密码错误");
        MSG_MAP.put(102, "账号已被禁用");
        MSG_MAP.put(103, "创建新用户失败");
        MSG_MAP.put(104, "玩家账号不存在");
        MSG_MAP.put(105, "服务端返回数据超时（下分）");
        MSG_MAP.put(111, "代理商点数不足");
        MSG_MAP.put(121, "订单号重复");
        MSG_MAP.put(1000, "代理商户号代码不存在");
        MSG_MAP.put(1001, "代理商户号已被冻结");
        MSG_MAP.put(1011, "解密失败");
        MSG_MAP.put(1012, "签名验证失败");
        MSG_MAP.put(10000, "IP限制");
        MSG_MAP.put(10001, "参数缺失");
        MSG_MAP.put(10002, "接口不存在");
        MSG_MAP.put(10003, "未知性错误");
        MSG_MAP.put(10004, "接口调用已过期");
        MSG_MAP.put(10005, "平台维护");
    }

    public boolean isSuccess() {
        return null != code && code.equals(0);
    }

    public String getCodeMsg() {
        if (null == this.code) {
            return null;
        }
        return MSG_MAP.get(this.code);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "data{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
