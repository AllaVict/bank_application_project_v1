package com.bank.core.util;

public interface Converter<F, T>  {
    T convert(F object);
    default T convert(F fromObject, T toObject) {
        return toObject;
    }

}

