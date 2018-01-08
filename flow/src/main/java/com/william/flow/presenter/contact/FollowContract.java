package com.william.flow.presenter.contact;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.UserCard;

/**
 * 关注的接口定义
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(ApplyModel model);
    }

    interface View extends BaseContract.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
