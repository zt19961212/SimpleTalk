package com.william.flow.presenter.group;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.db.Group;

/**
 * 我的群列表契约
 */
public interface GroupsContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
