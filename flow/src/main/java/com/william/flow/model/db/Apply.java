package com.william.flow.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by hasee on 2018/1/3.
 */

@Table(database = AppDatabase.class)
public class Apply extends BaseDbModel<Apply> implements Serializable {
    public static final int TYPE_ADD_USER = 1; //添加好友
    public static final int TYPE_ADD_GROUP = 2;//添加群
    public static final int TYPE_NOT_CONFIRMED = -1;//未确认
    public static final int TYPE_CONFIRMED = 0; //已确认
    @PrimaryKey
    private String id;
    //描述
    @Column
    private String desc;
    //内容
    @Column
    private String content;
    //附件
    @Column
    private String attach;
    //申请类型
    @Column
    private int type;
    //申请人id
    @Column
    private String applicantId;
    //目标id
    @Column
    private String targetId;
    //备注
    @Column
    private String remark;
    //请求是否已确认
    @Column
    private int confirmType;
    //创建时间
    @Column
    private Date createTime;
    //更新时间
    @Column
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(int confirmType) {
        this.confirmType = confirmType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Apply apply = (Apply) obj;
        return type == apply.type
                && confirmType == apply.confirmType
                && Objects.equals(id, apply.id)
                && Objects.equals(desc, apply.desc)
                && Objects.equals(attach, apply.attach)
                && Objects.equals(targetId, apply.targetId)
                && Objects.equals(applicantId, apply.applicantId)
                && Objects.equals(remark, apply.remark)
                && Objects.equals(createTime, apply.createTime)
                && Objects.equals(updateTime, apply.updateTime);
    }

    @Override
    public int hashCode() {
        return id!=null?id.hashCode():0;
    }

    @Override
    public boolean isSame(Apply old) {
        return id.equalsIgnoreCase(old.id);
    }

    @Override
    public boolean isUiContentSame(Apply old) {
       return old==this||((confirmType==old.confirmType)&&(type==old.type)&&applicantId.equalsIgnoreCase(old.applicantId));
    }
}
