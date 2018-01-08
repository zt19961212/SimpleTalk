package com.william.common.rxjava;

import io.reactivex.disposables.Disposable;

/**
 * Created by hasee on 2018/1/2.
 */

public  abstract class Task<T> implements mission<T>{
    private Disposable disposable;
    public Task()
    {

    }
void setDisposable(Disposable disposable)
{
    this.disposable=disposable;
}

    @Override
    public void doUITask() {

    }

    @Override
    public void doUITask(T t) {

    }

    @Override
    public void doIOTask() {

    }

    @Override
    public void doIOTask(T t) {

    }

    @Override
    public T getResultFromTask() {
        return null;
    }
}
