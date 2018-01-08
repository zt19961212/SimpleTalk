package com.william.flow.presenter.search;


import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.data.helper.GroupHelper;
import com.william.flow.model.card.GroupCard;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索群的逻辑实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>> {
    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            // 如果有上一次的请求，并且没有取消，
            // 则调用取消请求操作
            call.cancel();
        }

        searchCall = GroupHelper.search(content, this);
    }


    @Override
    public void onDataNotAvailable(final int strRes) {
        // 搜索失败
        final SearchContract.GroupView view = getView();
        if (view != null) {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.showError(strRes);
                }
            });

        }
    }

    @Override
    public void onDataLoaded(final List<GroupCard> groupCards) {
        // 搜索成功
        final SearchContract.GroupView view = getView();
        if (view != null) {
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.onSearchDone(groupCards);
                }
            });

        }
    }
}
