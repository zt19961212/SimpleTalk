package com.william.simpletalk.fragments.search;


import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.william.common.app.BaseApplication;
import com.william.common.app.PresenterFragment;
import com.william.common.widget.EmptyView;
import com.william.common.widget.PortraitView;
import com.william.common.widget.recycler.RecyclerAdapter;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.GroupCard;
import com.william.flow.model.db.Apply;
import com.william.flow.persistence.Account;
import com.william.flow.presenter.group.GroupJoinContract;
import com.william.flow.presenter.group.GroupJoinPresenter;
import com.william.flow.presenter.search.SearchContract;
import com.william.flow.presenter.search.SearchGroupPresenter;
import com.william.simpletalk.R;
import com.william.simpletalk.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.GroupView {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<GroupCard> mAdapter;

    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard userCard) {
                // 返回cell的布局id
                return R.layout.cell_search_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        // 数据成功的情况下返回数据
        mAdapter.replace(groupCards);
        // 如果有数据，则是OK，没有数据就显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 每一个Cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> implements GroupJoinContract.View {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_join)
        ImageView mJoin;
        GroupJoinContract.Presenter presenter;

        public ViewHolder(View itemView) {
            super(itemView);
            presenter = new GroupJoinPresenter(this);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            // 加入时间判断是否加入群
            mJoin.setEnabled(groupCard.getJoinTime() == null);
        }

        @OnClick(R.id.im_join)
        void onJoinClick() {
            ApplyModel model = new ApplyModel();
            model.setApplicantId(Account.getUserId());
            model.setTargetId(mData.getId());
            model.setType(Apply.TYPE_ADD_GROUP);
            model.setDescription(String.format("%s申请加入群",Account.getUser().getName()));
            model.setRemark("备注");
            model.setConfirmType(Apply.TYPE_NOT_CONFIRMED);
            presenter.applyJoinGroup(model);
        }

        @Override
        public void showError(@StringRes int str) {
            // 更改当前界面状态
            if (mJoin.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) mJoin.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 设置进去
            mJoin.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void onApplySuccessed() {
            if (mJoin.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mJoin.getDrawable()).stop();
                // 设置为默认的
              mJoin.setImageResource(R.drawable.sel_opt_done_add);
            }
            BaseApplication.showToast(R.string.toast_apply_join_group_success);
        }

        @Override
        public void setPresenter(GroupJoinContract.Presenter presenter) {
            this.presenter = presenter;
        }
    }
}
