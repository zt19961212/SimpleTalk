package com.william.flow.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.william.common.flow.model.Author;

import java.util.Date;
import java.util.Objects;

/**
 * 用户信息表
 */
@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author {
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    // 主键
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;

    // 我对某人的备注信息，也应该写入到数据库中
    @Column
    private String alias;

    // 用户关注人的数量
    @Column
    private int follows;

    // 用户粉丝的数量
    @Column
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Column
    private boolean isFollow;
    @Column
    private boolean isFriend;
    // 时间字段
    @Column
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return sex == user.sex
                && follows == user.follows
                && following == user.following
                && isFollow == user.isFollow
                &&isFriend==user.isFriend
                && Objects.equals(id, user.id)
                && Objects.equals(name, user.name)
                && Objects.equals(phone, user.phone)
                && Objects.equals(portrait, user.portrait)
                && Objects.equals(desc, user.desc)
                && Objects.equals(alias, user.alias)
                && Objects.equals(modifyTime, user.modifyTime);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(User old) {
        // 主要关注Id即可
        return this == old || Objects.equals(id, old.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        // 显示的内容是否一样，主要判断 名字，头像，性别，是否已经关注
        return this == old || (
                Objects.equals(name, old.name)
                        && Objects.equals(portrait, old.portrait)
                        && Objects.equals(sex, old.sex)
                        && Objects.equals(isFollow, old.isFollow)

        );
    }
}
