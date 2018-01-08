package com.william.common.rxjava;

/**
 * Created by hasee on 2018/1/2.
 */

public interface mission<T> {
    void doUITask();
    void doUITask(T t);
    void doIOTask();
    void doIOTask(T t);
    T getResultFromTask();
}
