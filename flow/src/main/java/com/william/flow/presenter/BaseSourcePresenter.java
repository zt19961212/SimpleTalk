package com.william.flow.presenter;


import com.william.common.flow.data.DataSource;
import com.william.common.flow.data.DbDataSource;
import com.william.common.flow.presenter.BaseContract;
import com.william.common.flow.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的Presenter定义
 */
public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
