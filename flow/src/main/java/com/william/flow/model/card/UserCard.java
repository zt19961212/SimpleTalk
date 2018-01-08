package com.william.flow.model.card;


import android.os.Parcel;
import android.os.Parcelable;

import com.william.common.flow.model.Author;
import com.william.flow.model.db.User;

import java.util.Date;

/**
 * 用户卡片，用于接收服务器返回
 */
public class UserCard implements Author, Parcelable {
    private String id;
    private String name;
    private String phone;
    private String portrait;
    private String desc;
    private int sex = 0;

    // 用户关注人的数量
    private int follows;

    // 用户粉丝的数量
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    private boolean isFollow;
    //我与当前user是否为好友
private boolean isFriend;
    // 用户信息最后的更新时间
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    // 缓存一个对应的User, 不能被GSON框架解析使用ø
    private transient User user;

    public User build() {
        if (user == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPortrait(portrait);
            user.setPhone(phone);
            user.setDesc(desc);
            user.setSex(sex);
            user.setFollow(isFollow);
            user.setFollows(follows);
            user.setFriend(isFriend);
            user.setFollowing(following);
            user.setModifyTime(modifyTime);
            this.user = user;
        }
        return user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.portrait);
        dest.writeString(this.desc);
        dest.writeInt(this.sex);
        dest.writeInt(this.follows);
        dest.writeInt(this.following);
        dest.writeByte(this.isFollow ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFriend ? (byte) 1 : (byte) 0);
        dest.writeLong(this.modifyTime != null ? this.modifyTime.getTime() : -1);
    }

    public UserCard() {
    }

    protected UserCard(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.portrait = in.readString();
        this.desc = in.readString();
        this.sex = in.readInt();
        this.follows = in.readInt();
        this.following = in.readInt();
        this.isFollow = in.readByte() != 0;
        this.isFriend = in.readByte() != 0;
        long tmpModifyTime = in.readLong();
        this.modifyTime = tmpModifyTime == -1 ? null : new Date(tmpModifyTime);
    }

    public static final Parcelable.Creator<UserCard> CREATOR = new Parcelable.Creator<UserCard>() {
        @Override
        public UserCard createFromParcel(Parcel source) {
            return new UserCard(source);
        }

        @Override
        public UserCard[] newArray(int size) {
            return new UserCard[size];
        }
    };
}
