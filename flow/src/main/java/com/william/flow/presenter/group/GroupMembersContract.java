package com.william.flow.presenter.group;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.db.view.MemberUserModel;

/**
 * 群成员的契约
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();

    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}
