package com.william.flow.presenter.group;

import com.william.common.flow.presenter.BaseContract;
import com.william.flow.model.api.apply.ApplyModel;

/**
 * 申请加入群的契约
 */

public interface GroupJoinContract {
    interface Presenter extends BaseContract.Presenter {

        void applyJoinGroup(ApplyModel model);


    }

    interface View extends BaseContract.View<Presenter> {
        void onApplySuccessed();


    }
}
