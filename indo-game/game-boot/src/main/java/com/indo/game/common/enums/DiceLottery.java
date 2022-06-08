package com.indo.game.common.enums;

public enum DiceLottery {
//    鱼 红色 一点
//    虾 绿色 二点
//    葫芦 蓝色 三点
//    金钱 蓝色 四点
//    蟹 绿色 五点
//    鸡 红色 六点
    dice_one("鱼", "1"),
    dice_two("虾", "2"),
    dice_three("葫芦", "3"),
    dice_four("金钱", "4"),
    dice_five("蟹", "5"),
    dice_six("鸡", "6");

    private String keyName;
    private String keyValue;

    private DiceLottery(String keyName, String keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
    }

    public String getKeyName() {
        return keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    public String getKeyValue() {
        return keyValue;
    }
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
