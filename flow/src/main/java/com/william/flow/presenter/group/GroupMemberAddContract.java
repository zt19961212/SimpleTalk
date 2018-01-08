package com.william.flow.presenter.group;


import com.william.common.flow.presenter.BaseContract;

/**
 * 群成员添加的契约
 */
public interface GroupMemberAddContract {
    interface Presenter extends BaseContract.Presenter {
        // 提交成员
        void submit();
        //设置标识以判断是哪个界面调用了presenter
        void setTag(String tag);
        // 更改一个Model的选中状态
        void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected);
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, GroupCreateContract.ViewModel> {

        // 添加群成员成功
        void onAddedSucceed();

        // 获取群的Id
        String getGroupId();
    }
}
