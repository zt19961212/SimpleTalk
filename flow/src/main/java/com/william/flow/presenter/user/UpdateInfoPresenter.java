package com.william.flow.presenter.user;

import android.text.TextUtils;

import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.Flow;
import com.william.flow.R;
import com.william.flow.data.helper.UserHelper;
import com.william.flow.model.api.user.UserUpdateModel;
import com.william.flow.model.card.UserCard;
import com.william.flow.model.db.User;
import com.william.flow.network.UploadHelper;


public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();

        final UpdateInfoContract.View view = getView();

        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            // 上传头像
           Flow.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        // 上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        // 构建Model
                        UserUpdateModel model = new UserUpdateModel("", url, desc,
                                isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        // 进行网络请求，上传
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        // 强制执行在主线程中
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                view.updateSucceed();
            }
        });

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        // 强制执行在主线程中
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                view.showError(strRes);
            }
        });

    }
}
