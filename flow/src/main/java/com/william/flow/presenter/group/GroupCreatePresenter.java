package com.william.flow.presenter.group;

import android.text.TextUtils;

import com.william.common.flow.data.DataSource;
import com.william.common.flow.presenter.BaseRecyclerPresenter;
import com.william.common.rxjava.RxUtil;
import com.william.common.rxjava.Task;
import com.william.flow.Flow;
import com.william.flow.R;
import com.william.flow.data.helper.GroupHelper;
import com.william.flow.data.helper.UserHelper;
import com.william.flow.model.api.group.GroupCreateModel;
import com.william.flow.model.card.GroupCard;
import com.william.flow.model.db.view.UserSampleModel;
import com.william.flow.network.UploadHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群创建界面的Presenter
 */
public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // 加载
     Flow.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();

        // 判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }

        // 上传图片
       Flow.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url))
                    return;
                // 进行网络请求
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected)
            users.add(model.author.getId());
        else
            users.remove(model.author.getId());
    }

    // 同步上传操作
    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            // 切换到UI线程 提示信息
            RxUtil.doOnUITask(new Task() {
                @Override
                public void doUITask() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    @Override
    public void onDataLoaded(GroupCard groupCard) {
        // 成功
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 失败情况
        RxUtil.doOnUITask(new Task() {
            @Override
            public void doUITask() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });

    }
}
