package com.asfursov.agrocom.model;

public enum Role {
    GUARD(1),
    WEIGH(2),
    UNLOAD(4);
    private int value;
    Role(int newValue){
        this.value=newValue;
    }

    public int getValue() {
        return value;
    }
    public String getName(){
        switch (this){
            case GUARD:  return "Охранник";
            case WEIGH:  return "Весовщик";
            case UNLOAD: return "Оператор выгрузки";
        };
        return "Не определено";
    }
}
