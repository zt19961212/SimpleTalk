package com.william.flow.presenter.contact;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.db.User;


public interface ContactContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }
}
