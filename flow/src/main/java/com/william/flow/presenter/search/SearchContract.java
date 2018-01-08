package com.william.flow.presenter.search;


import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.card.GroupCard;
import com.william.flow.model.card.UserCard;

import java.util.List;

/**
 *搜索契约
 */
public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }

}
