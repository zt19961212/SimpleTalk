package com.william.flow.presenter.account;

import android.text.TextUtils;

import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.R;
import com.william.flow.data.helper.AccountHelper;
import com.william.flow.model.api.account.LoginModel;
import com.william.flow.model.db.User;
import com.william.flow.persistence.Account;


/**
 * 登录的逻辑实现
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        final LoginContract.View view = getView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            // 尝试传递PushId
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if (view == null)
            return;
        // 强制执行在主线程中
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                view.loginSuccess();
            }
        });

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 网络请求告知注册失败
        final LoginContract.View view = getView();
        if (view == null)
            return;
        // 此时是从网络回送回来的，并不保证处于主线程
        // 强制执行在主线程中
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                // 调用主界面注册失败显示错误
                view.showError(strRes);
            }
        });

    }
}
