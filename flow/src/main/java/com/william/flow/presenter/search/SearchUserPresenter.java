package com.william.flow.presenter.search;


import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BasePresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.data.helper.UserHelper;
import com.william.flow.model.card.UserCard;

import java.util.List;

import retrofit2.Call;

/**
 * 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {
    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
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

        searchCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        // 搜索成功
        final SearchContract.UserView view = getView();
        if(view!=null){
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.onSearchDone(userCards);
                }
            });

        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 搜索失败
        final SearchContract.UserView view = getView();
        if(view!=null){
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    view.showError(strRes);
                }
            });

        }
    }
}
