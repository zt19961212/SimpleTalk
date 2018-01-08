package com.william.flow.presenter.group;


import android.text.TextUtils;

import com.william.common.Common;
import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BaseRecyclerPresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.Flow;
import com.william.flow.R;
import com.william.flow.data.helper.GroupHelper;
import com.william.flow.data.helper.UserHelper;
import com.william.flow.model.api.group.GroupMemberAddModel;
import com.william.flow.model.card.GroupMemberCard;
import com.william.flow.model.db.view.MemberUserModel;
import com.william.flow.model.db.view.UserSampleModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群成员添加的逻辑
 */
public class GroupMemberAddPresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel,
        GroupMemberAddContract.View> implements GroupMemberAddContract.Presenter,
        DataSource.Callback<List<GroupMemberCard>> {
    private Set<String> users = new HashSet<>();
    private String tag;
    private String applyId;

    public GroupMemberAddPresenter(GroupMemberAddContract.View view) {
        super(view);

    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void start() {
        super.start();

        if (tag.equals(Common.Constance.TAG_GROUPMEMBERADD)) {
            // 如果是创建群则加载可以添加的用户
            Flow.runOnAsync(loader);
        }

    }

    @Override
    public void submit() {
        GroupMemberAddContract.View view = getView();
        view.showLoading();

        // 判断参数
        if (users.size() == 0) {
            view.showError(R.string.label_group_member_add_invalid);
            return;
        }

        // 进行网络请求
        GroupMemberAddModel model = new GroupMemberAddModel(users);
        if (!TextUtils.isEmpty(applyId)) {
            model.setApplyId(applyId);
        }
        GroupHelper.addMembers(view.getGroupId(), model, this);
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {


        if (isSelected)
            users.add(model.author.getId());
        else
            users.remove(model.author.getId());
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMemberAddContract.View view = getView();
            if (view == null)
                return;

            // 我所有的联系人
            List<UserSampleModel> contact = UserHelper.getSampleContact();
            // 以及在群里的人
            List<MemberUserModel> members = GroupHelper.getMemberUsers(view.getGroupId(), -1);

            for (MemberUserModel member : members) {
                // 如果有就进行移除
                int index = indexOfUserContact(contact, member.userId);
                if (index >= 0)
                    contact.remove(index);
            }

            // 返回一个界面显示的Model
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel model : contact) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = model;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    // 联系人中找用户的位置坐标
    private int indexOfUserContact(List<UserSampleModel> contact, String userId) {
        int index = 0;
        for (UserSampleModel model : contact) {
            if (model.id.equalsIgnoreCase(userId))
                return index;
            index++;
        }
        return -1;
    }

    @Override
    public void onDataLoaded(List<GroupMemberCard> groupMemberCards) {
        // 成功
        GroupMemberAddContract.View view = getView();
        if (view == null)
            return;
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                GroupMemberAddContract.View view = getView();
                if (view == null)
                    return;
                view.onAddedSucceed();
            }
        });

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 失败
        GroupMemberAddContract.View view = getView();
        if (view == null)
            return;
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                GroupMemberAddContract.View view = getView();
                if (view == null)
                    return;
                view.showError(strRes);
            }
        });

    }
}
