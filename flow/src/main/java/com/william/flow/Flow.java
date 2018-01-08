package com.william.flow;

import android.support.annotation.StringRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.william.common.app.BaseApplication;
import com.william.common.flow.data.DataSource;
import com.william.flow.data.apply.ApplyCenter;
import com.william.flow.data.apply.ApplyDispatcher;
import com.william.flow.data.group.GroupCenter;
import com.william.flow.data.group.GroupDispatcher;
import com.william.flow.data.message.MessageCenter;
import com.william.flow.data.message.MessageDispatcher;
import com.william.flow.data.user.UserCenter;
import com.william.flow.data.user.UserDispatcher;
import com.william.flow.model.api.PushModel;
import com.william.flow.model.api.RspModel;
import com.william.flow.model.card.ApplyCard;
import com.william.flow.model.card.FollowCard;
import com.william.flow.model.card.GroupMemberAddCard;
import com.william.flow.model.card.GroupMemberCard;
import com.william.flow.model.card.JoinGroupCard;
import com.william.flow.model.card.MessageCard;
import com.william.flow.model.card.UserCard;
import com.william.flow.model.db.Apply;
import com.william.flow.persistence.Account;
import com.william.flow.utils.DBFlowExclusionStrategy;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Flow {
    private static final String TAG = Flow.class.getSimpleName();
    // 单例模式
    private static final Flow instance;
    // 全局的线程池
    private final Executor executor;
    // 全局的Gson
    private final Gson gson;


    static {
        instance = new Flow();
    }

    private Flow() {
        // 新建一个4个线程的线程池
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置一个过滤器，数据库级别的Model不进行Json转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory 中的初始化
     */
    public static void setup() {
        // 初始化数据库
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true) // 数据库初始化的时候就开始打开
                .build());

        // 持久化的数据进行初始化
        Account.load(app());
    }

    /**
     * 返回全局的Application
     *
     * @return Application
     */
    public static BaseApplication app() {
        return BaseApplication.getInstance();
    }

    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        // 拿到单例，拿到线程池，然后异步执行
        instance.executor.execute(runnable);
    }

    /**
     * 返回一个全局的Gson，在这可以进行Gson的一些全局的初始化
     *
     * @return Gson
     */
    public static Gson getGson() {
        return instance.gson;
    }


    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                BaseApplication.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }


    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {
BaseApplication.getInstance().finishAll();
    }


    /**
     * 处理推送来的消息
     *
     * @param str 消息
     */
    public static void dispatchPush(String str) {
        // 首先检查登录状态
        if (!Account.isLogin())
            return;

        PushModel model = PushModel.decode(str);
        if (model == null)
            return;

        // 对推送集合进行遍历
        for (PushModel.Entity entity : model.getEntities()) {
            Log.e(TAG, "dispatchPush-Entity:" + entity.toString());

            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    // 退出情况下，直接返回，并且不可继续
                    return;

                case PushModel.ENTITY_TYPE_MESSAGE: {
                    // 普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    // 好友添加
                    FollowCard followCard = getGson().fromJson(entity.content, FollowCard.class);
                    ApplyCard applyCard = followCard.getApplyCard();
                    UserCard userCard = followCard.getUserCard();
                    String content;
                    if (userCard.isFriend()) {
                        getUserCenter().dispatch(userCard);
                    }

                    getApplyCenter().dispatch(applyCard);

                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    // 添加群
                   GroupMemberAddCard memberAddCard = getGson().fromJson(entity.content, GroupMemberAddCard.class);
                    getGroupCenter().dispatch(memberAddCard.getGroupCard());
                    ApplyCard applyCard=memberAddCard.getApplyCard();
                    if(applyCard!=null)
                    {
                        getApplyCenter().dispatch(applyCard);
                    }
                    break;
                }
                case PushModel.ENTITY_TYPE_APPLY_JOIN_GROUP:
                    JoinGroupCard joinGroupCard = getGson().fromJson(entity.content, JoinGroupCard.class);
                    ApplyCard applyCard = joinGroupCard.getApplyCard();
                    if (applyCard.getConfirmType() == Apply.TYPE_CONFIRMED) {

                        getGroupCenter().dispatch(joinGroupCard.getMemberCard());
                    }
                    getApplyCenter().dispatch(applyCard);
                    break;

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    // 群成员变更, 回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    // 把数据集合丢到数据中心处理
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {
                    // TODO 成员退出的推送
                }

            }
        }
    }


    /**
     * 获取一个用户中心的实现类
     *
     * @return 用户中心的规范接口
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    /**
     * 获取一个消息中心的实现类
     *
     * @return 消息中心的规范接口
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }


    /**
     * 获取一个群处理中心的实现类
     *
     * @return 群中心的规范接口
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }

    /**
     * 获取申请处理中心实现类
     *
     * @return
     */
    public static ApplyCenter getApplyCenter() {
        return ApplyDispatcher.getInstance();
    }
}
