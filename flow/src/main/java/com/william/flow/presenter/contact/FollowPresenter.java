package com.william.flow.presenter.contact;


import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.data.helper.UserHelper;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.UserCard;

/**
 * 关注的逻辑实现
 */
public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(ApplyModel model) {
        start();
        UserHelper.follow(model, this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        // 成功
        final FollowContract.View view = getView();
        if (view != null) {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.onFollowSucceed(userCard);
                }
            });

        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.showError(strRes);
                }
            });

        }
    }
}
