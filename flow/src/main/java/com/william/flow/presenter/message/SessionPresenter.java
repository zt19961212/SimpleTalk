package com.william.flow.presenter.message;

import android.support.v7.util.DiffUtil;

import com.william.flow.data.message.SessionDataSource;
import com.william.flow.data.message.SessionRepository;
import com.william.flow.model.db.Session;
import com.william.flow.presenter.BaseSourcePresenter;
import com.william.flow.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的Presenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;

        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 刷新界面
        refreshData(result, sessions);
    }
}
