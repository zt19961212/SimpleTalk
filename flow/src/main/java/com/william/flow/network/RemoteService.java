package com.william.flow.network;


import com.william.flow.model.api.RspModel;
import com.william.flow.model.api.account.AccountRspModel;
import com.william.flow.model.api.account.LoginModel;
import com.william.flow.model.api.account.RegisterModel;
import com.william.flow.model.api.apply.ApplyModel;
import com.william.flow.model.api.group.GroupCreateModel;
import com.william.flow.model.api.group.GroupMemberAddModel;
import com.william.flow.model.api.message.MsgCreateModel;
import com.william.flow.model.api.user.UserUpdateModel;
import com.william.flow.model.card.ApplyCard;
import com.william.flow.model.card.FollowCard;
import com.william.flow.model.card.GroupCard;
import com.william.flow.model.card.GroupMemberCard;
import com.william.flow.model.card.MessageCard;
import com.william.flow.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求的所有的接口
 */
public interface RemoteService {

    /**
     * 注册接口
     *
     * @param model 传入的是RegisterModel
     * @return 返回的是RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备Id
     *
     * @param pushId 设备Id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 用户更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    // 用户搜索的接口
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    // 用户关注接口
    @POST("user/follow")
    Call<RspModel<FollowCard>> userFollow(@Body ApplyModel model);

    // 获取联系人列表
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    // 查询某人的信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 发送消息的接口
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 我的群的成员列表
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 给群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,

                                                         @Body GroupMemberAddModel model);
    //加入一个群
    @POST("group/applyJoin")
    Call<RspModel<ApplyCard>> joinGroup(@Body ApplyModel model);
    //查询一条申请
    @GET("apply/search/{id}")
    Call<RspModel<ApplyCard>> searchApply(@Path("id") String id);
}
