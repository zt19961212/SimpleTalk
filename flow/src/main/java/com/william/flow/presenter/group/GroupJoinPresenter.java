package com.william.flow.presenter.group;

import android.support.annotation.StringRes;

import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.data.helper.GroupHelper;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.ApplyCard;


public class GroupJoinPresenter extends BasePresenter<GroupJoinContract.View>
        implements GroupJoinContract.Presenter, DataSource.Callback<ApplyCard> {


    public GroupJoinPresenter(GroupJoinContract.View view) {
        super(view);

    }



    @Override
    public void applyJoinGroup(ApplyModel model) {
        start();
        GroupHelper.applyJoinGroup(model,this);
    }



    @Override
    public void onDataLoaded(final ApplyCard applyCard) {
        final    GroupJoinContract.View view = getView();
        if (view != null)
        {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {

                    view.onApplySuccessed();


                }
            });
        }
    }


    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
     final    GroupJoinContract.View view = getView();
        if (view != null)
        {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.showError(strRes);
                }
            });
        }
    }
}
