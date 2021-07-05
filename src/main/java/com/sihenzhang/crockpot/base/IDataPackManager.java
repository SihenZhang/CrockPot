package com.sihenzhang.crockpot.base;

public interface IDataPackManager {
    String serialize();

    void deserialize(String data);
}
