package com.william.simpletalk;

import android.content.Context;
import android.content.Intent;

import com.igexin.sdk.PushManager;
import com.william.common.app.BaseApplication;
import com.william.flow.Flow;
import com.william.simpletalk.activities.AccountActivity;

/**
 * Created by hasee on 2018/1/2.
 */

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
      Flow.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountView(Context context) {

        // 登录界面的显示
        showToast(getString(R.string.toast_account_logout));
        Intent intent=new Intent(context, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
