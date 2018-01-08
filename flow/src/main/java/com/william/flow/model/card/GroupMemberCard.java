package com.william.flow.model.card;

import com.william.flow.model.db.Group;
import com.william.flow.model.db.GroupMember;
import com.william.flow.model.db.User;

import java.util.Date;

/**
 * 群成员卡片信息
 */

public class GroupMemberCard {
    private String id;
    private String alias;
    private boolean isAdmin;
    private boolean isOwner;
    private String userId;
    private String groupId;
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public GroupMember build(Group group, User user) {
        GroupMember member = new GroupMember();
        member.setId(this.id);
        member.setAlias(this.alias);
        member.setAdmin(this.isAdmin);
        member.setOwner(this.isOwner);
        member.setModifyTime(this.modifyTime);
        member.setGroup(group);
        member.setUser(user);
        return member;
    }
}
