package com.william.simpletalk.fragments.main;


import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.william.common.Common;
import com.william.common.app.BaseApplication;
import com.william.common.app.PresenterFragment;
import com.william.common.face.Face;
import com.william.common.utils.DateTimeUtil;
import com.william.common.widget.EmptyView;
import com.william.common.widget.PortraitView;
import com.william.common.widget.recycler.RecyclerAdapter;
import com.william.flow.Flow;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.card.ApplyCard;
import com.william.flow.model.card.UserCard;
import com.william.flow.model.db.Apply;
import com.william.flow.model.db.Session;
import com.william.flow.persistence.Account;
import com.william.flow.presenter.contact.FollowContract;
import com.william.flow.presenter.contact.FollowPresenter;
import com.william.flow.presenter.group.GroupCreateContract;
import com.william.flow.presenter.group.GroupMemberAddContract;
import com.william.flow.presenter.group.GroupMemberAddPresenter;
import com.william.flow.presenter.message.SessionContract;
import com.william.flow.presenter.message.SessionPresenter;
import com.william.simpletalk.R;
import com.william.simpletalk.activities.MessageActivity;

import net.qiujuer.genius.ui.widget.Button;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<Session> mAdapter;

    private Boolean isAgree;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                Apply apply = session.getApply();
                if (apply != null) {
                    if(apply.getType()==Apply.TYPE_ADD_USER)
                    return R.layout.cell_notify_friend;
                    else
                        return R.layout.cell_notify_group;
                } else {
                    // 返回cell的布局id
                    return R.layout.cell_chat_list;
                }

            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                switch (viewType) {
                    case R.layout.cell_notify_friend:
                        return new ActiveFragment.NotifyFriendHolder(root);
                    case R.layout.cell_notify_group:
                        return new ActiveFragment.NotifyGroupHolder(root);
                    case R.layout.cell_chat_list:
                    default:
                        return new ActiveFragment.ViewHolder(root);
                }

            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), session);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    // 界面数据渲染
    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @Nullable
        @BindView(R.id.txt_time)
        TextView mTime;
        protected String receiverId;
        protected Session session;
        protected String applyId;
       protected Apply apply;
        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            this.session = session;
            mName.setText(session.getTitle());
            if (mTime != null) {
                mTime.setText(DateTimeUtil.getSampleDate(session.getModifyTime()));
            }
          apply = session.getApply();
            if (apply != null) {
                applyId = apply.getId();
                receiverId = apply.getApplicantId();
                if (apply.getType() == Apply.TYPE_ADD_USER) {

                    mPortraitView.setup(Glide.with(ActiveFragment.this), R.drawable.friend, null);
                } else if (apply.getType() == Apply.TYPE_ADD_GROUP) {

                    mPortraitView.setup(Glide.with(ActiveFragment.this), R.drawable.group, null);
                }
                mContent.setText(session.getContent());
            } else {
                mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
                String str = TextUtils.isEmpty(session.getContent()) ? "" : session.getContent();
                Spannable spannable = new SpannableString(str);
                // 解析表情
                Face.decode(mContent, spannable, (int) mContent.getTextSize());
                // 把内容设置到布局上
                mContent.setText(spannable);
            }


        }
    }

    class NotifyFriendHolder extends ViewHolder implements FollowContract.View {
        @BindView(R.id.btn_agree)
        Button btnAgree;
        private FollowContract.Presenter presenter;


        public NotifyFriendHolder(View itemView) {
            super(itemView);
            presenter = new FollowPresenter(this);
        }

        @Override
        protected void onBind(Session session) {
            super.onBind(session);
            if (apply != null) {

                applyId = apply.getId();
                if (apply.getConfirmType() == Apply.TYPE_CONFIRMED) {

                    if (apply.getTargetId().equalsIgnoreCase(Account.getUserId())) {
                        btnAgree.setText(getString(R.string.label_notify_agree));

                        btnAgree.setEnabled(false);
                    } else {
                        btnAgree.setVisibility(View.GONE);
                    }
                }
            }
        }

        @OnClick(R.id.btn_agree)
        void onAgreeClick() {
            ApplyModel model = new ApplyModel();
            model.setId(applyId);
            model.setApplicantId(Account.getUserId());
            model.setTargetId(receiverId);
            presenter.follow(model);
        }

        @Override
        public void showError(@StringRes int str) {
            BaseApplication.showToast(getString(str));
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            btnAgree.setText(getString(R.string.label_notify_agree));
            btnAgree.setEnabled(false);
        }


    }

    class NotifyGroupHolder extends ViewHolder implements GroupMemberAddContract.View {
        @BindView(R.id.btn_agree)
        Button btnAgree;
        private GroupMemberAddContract.Presenter presenter;
        String applyId;


        public NotifyGroupHolder(View itemView) {
            super(itemView);
            presenter = new GroupMemberAddPresenter(this);
            presenter.setTag(Common.Constance.TAG_GROUPJOIN);
        }

        @Override
        protected void onBind(Session session) {
            super.onBind(session);
            if (session.getApply() != null) {
                apply = session.getApply();
                applyId = apply.getId();
                if (apply.getConfirmType() == Apply.TYPE_CONFIRMED) {

                    if (apply.getTargetId().equalsIgnoreCase(Account.getUserId())) {
                        btnAgree.setText(getString(R.string.label_notify_agree));

                        btnAgree.setEnabled(false);
                    } else {
                        btnAgree.setVisibility(View.GONE);
                    }
                }
            }
        }

        @OnClick(R.id.btn_agree)
        void onAgreeClick() {
            Set<String> users = new HashSet<>();
            users.add(apply.getApplicantId());
            ( (GroupMemberAddPresenter)presenter).setUsers(users);
            ( (GroupMemberAddPresenter)presenter).setApplyId(applyId);
            presenter.submit();
        }

        @Override
        public void showError(@StringRes int str) {
            BaseApplication.showToast(getString(str));
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void setPresenter(GroupMemberAddContract.Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onAddedSucceed() {
            btnAgree.setText(getString(R.string.label_notify_agree));
            btnAgree.setEnabled(false);
            apply.setConfirmType(Apply.TYPE_CONFIRMED);
            ApplyCard applyCard=new ApplyCard(apply);
            Flow.getApplyCenter().dispatch(applyCard);
        }

        @Override
        public String getGroupId() {
            return apply.getTargetId();
        }

        @Override
        public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
            return null;
        }

        @Override
        public void onAdapterDataChanged() {

        }



    }
}
