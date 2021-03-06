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
                return "ЗАЕЗД";
            case LEAVE:
                return "ВЫЕЗД";
            case WEIGH_START:
                return "ЗАЕЗД НА ВЕСЫ";
            case WEIGH_END:
                return "ВЫЕЗД С ВЕСОВ";
            case UNLOAD_START:
                return "НАЧАЛО ВЫГРУЗКИ";
            case UNLOAD_END:
                return "ВЫГРУЗКА ОКОНЧЕНА";
        }
        return "Не определено";
    }

    public String getAPIName() {
        switch (this) {
            case ENTER:
                return "ENTER";
            case LEAVE:
                return "LEAVE";
            case WEIGH_START:
                return "WEIGH_START";
            case WEIGH_END:
                return "WEIGH_END";
            case UNLOAD_START:
                return "UNLOAD_START";
            case UNLOAD_END:
                return "UNLOAD_END";
        }
        return "DEFAULT";
    }

    public int getValue() {
        return value;
    }
}
