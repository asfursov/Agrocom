package com.asfursov.agrocom.model;

public enum OperationId {
    ENTER(1),
    LEAVE(2),
    WEIGH_START(3),
    WEIGH_END(4),
    UNLOAD_START(5),
    UNLOAD_END(6);

    private final int value;

    OperationId(int newValue) {
        this.value = newValue;
    }

    public String getName() {
        switch (this) {
            case ENTER:
                return "Заезд";
            case LEAVE:
                return "Выезд";
            case WEIGH_START:
                return "НАЧАТЬ";
            case WEIGH_END:
                return "Закончить";
            case UNLOAD_START:
                return "Начать";
            case UNLOAD_END:
                return "Закончить";
        }
        return "Не определено";
    }

    public String getAPIName() {
        switch (this) {
            case ENTER:
                return "ENTER";
            case LEAVE:
                return "LEAVE";
        }
        return "DEFAULT";
    }

    public int getValue() {
        return value;
    }
}
