package com.asfursov.agrocom.model;

public enum OperationId {
    ENTER(1),
    LEAVE(2);
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
