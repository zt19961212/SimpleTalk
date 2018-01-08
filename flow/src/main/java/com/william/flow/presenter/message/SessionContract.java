package com.william.flow.presenter.message;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.db.Session;

/**
 *会话契约
 */
public interface SessionContract {

    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
