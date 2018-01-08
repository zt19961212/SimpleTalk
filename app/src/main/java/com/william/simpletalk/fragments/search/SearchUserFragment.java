package com.william.simpletalk.fragments.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.william.common.Common;
import com.william.common.app.PresenterFragment;
import com.william.common.widget.EmptyView;
import com.william.common.widget.PortraitView;
import com.william.common.widget.recycler.RecyclerAdapter;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.UserCard;
import com.william.flow.model.db.Apply;
import com.william.flow.persistence.Account;
import com.william.flow.presenter.contact.FollowContract;
import com.william.flow.presenter.contact.FollowPresenter;
import com.william.flow.presenter.search.SearchContract;
import com.william.flow.presenter.search.SearchUserPresenter;
import com.william.simpletalk.R;
import com.william.simpletalk.activities.ContactAddActivity;
import com.william.simpletalk.activities.PersonalActivity;
import com.william.simpletalk.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;
private int position;
    private boolean isSend=false;
    private UserCard card;
    private String content,remark;
    private ViewHolder viewHolder;
    public SearchUserFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回cell的布局id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
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
        // Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        // 数据成功的情况下返回数据
        mAdapter.replace(userCards);
        // 如果有数据，则是OK，没有数据就显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Common.Constance.RESULT_FOLLOW)
        {
            isSend=data.getBooleanExtra("isSend",false);
            if(isSend)
            {
                content=data.getStringExtra("content");
                remark=data.getStringExtra("remark");
                viewHolder=(ViewHolder)mRecycler.getChildViewHolder(mRecycler.getChildAt(position));
               viewHolder.onFollowClick();

            }
        }
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        // 初始化Presenter
        return new SearchUserPresenter(this);
    }

    /**
     * 每一个Cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_follow)
        ImageView mFollow;

        FollowContract.Presenter mPresenter;


        public ViewHolder(View itemView) {
            super(itemView);
            // 当前View和Presenter绑定
          mPresenter=  new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this), userCard);
            mName.setText(userCard.getName());
            mFollow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            // 显示信息
            PersonalActivity.show(getContext(), mData.getId());
        }

        @OnClick(R.id.im_follow)
        void onFollowClick() {
            // 发起关注
          //  mPresenter.follow(mData.getId());
            position=(int)itemView.getTag();
            if(!isSend)
            {
                card=mData;
                Intent intent=new Intent(getActivity(), ContactAddActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean("isSend",false);
                bundle.putParcelable("userCard",mData);
                intent.putExtras(bundle);
                startActivityForResult(intent, Common.Constance.REQUEST_FOLLOW);
            }
            else
            {
                ApplyModel model=new ApplyModel();
                model.setApplicantId(Account.getUserId());
                model.setTargetId(card.getId());
                model.setRemark(remark);
                model.setType(Apply.TYPE_ADD_USER);
                model.setDescription(content);
                model.setConfirmType(Apply.TYPE_NOT_CONFIRMED);
                mPresenter.follow(model);
            }
        }

        @Override
        public void showError(int str) {
            // 更改当前界面状态
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
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
            mFollow.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            // 更改当前界面状态
            if (mFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mFollow.getDrawable()).stop();
                // 设置为默认的
                mFollow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }
    }
}
