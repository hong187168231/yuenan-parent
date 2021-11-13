package com.indo.game.pojo.entity.ae;

public class AeApiResponse {
    private Integer ret;
    private AeApiResponseData data;

    public boolean isSuccess() {
        return null != data && data.isSuccess();
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public AeApiResponseData getData() {
        return data;
    }

    public void setData(AeApiResponseData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AeApiResponse{" +
                "ret=" + ret +
                ", data=" + data +
                '}';
    }
}
