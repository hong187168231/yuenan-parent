package com.indo.game.common.constant;


public enum GameMoneyRecordEnum {
    /**
     *game_money_record 表中的枚举
     */
    AG_TYPE(1,"游戏类型为AG"),
    KY_TYPE(2,"游戏类型为开元"),
    ES_TYPE(3,"游戏类型为电竞"),
    AE_TYPE(4,"游戏类型为AE"),
    OPERATE_IN(1,"上分"),
    OPERATE_OUT(2,"下分"),
    SUCCESS(0,"after"),
    FAIL(1,"before"),
    AE_UP_SUCCESS(410,"AE上分成功"),
    AE_UP_FAIL(411,"AE上分失败"),
    AE_DOWN_SUCCESS(420,"AE下分成功"),
    AE_DOWN_FAIL(421,"AE下分失败"),
    AG_UP_SUCCESS(110,"AG上分成功"),
    AG_UP_FAIL(111,"AG上分失败"),
    AG_DOWN_SUCCESS(120,"AG下分成功"),
    AG_DOWN_FAIL(121,"AG下分失败"),
    KY_UP_SUCCESS(210,"开元上分成功"),
    KY_UP_FAIL(211,"开元上分失败"),
    KY_DOWN_SUCCESS(220,"开元下分成功"),
    KY_DOWN_FAIL(221,"开元下分失败"),
    ES_UP_SUCCESS(310,"ES上分成功"),
    ES_UP_FAIL(311,"ES上分失败"),
    ES_DOWN_SUCCESS(320,"ES下分成功"),
    ES_DOWN_FAIL(321,"ES下分失败")
    ;

    private Integer value;
    private String key;

    GameMoneyRecordEnum(Integer value, String key) {
        this.value = value;
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
