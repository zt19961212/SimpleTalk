package com.william.flow.model.card;

import com.william.flow.model.db.Apply;

import java.util.Date;

/**
 * 申请请求的Card, 用于推送一个申请请求
 */
public class ApplyCard {
    // 申请Id
    private String id;
    // 附件
    private String attach;
    //内容
    private String content;
    // 描述
    private String desc;
    //备注
    private String remark;
    // 目标的类型
    private int type;
    // 目标（群／人...的ID）
    private String targetId;
    // 申请人的Id
    private String applicantId;
    // 创建时间
    private Date createTime;
    private Date updateTime;
    //请求是否确认
private int confirmType;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(int confirmType) {
        this.confirmType = confirmType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
public ApplyCard(Apply apply)
{
    id=apply.getId();
    confirmType=apply.getConfirmType();
    createTime=apply.getCreateTime();
    updateTime=apply.getUpdateTime();
    content=apply.getContent();
    attach=apply.getAttach();
    applicantId=apply.getApplicantId();
    desc=apply.getDesc();
    remark=apply.getRemark();
    targetId=apply.getTargetId();
    type=apply.getType();
}
    public Apply build()
    {
        Apply apply=new Apply();
        apply.setId(id);
        apply.setConfirmType(confirmType);
        apply.setCreateTime(createTime);
        apply.setUpdateTime(updateTime);
        apply.setContent(content);
        apply.setAttach(attach);
        apply.setApplicantId(applicantId);
        apply.setDesc(desc);
        apply.setRemark(remark);
        apply.setTargetId(targetId);
        apply.setType(type);
        return apply;
    }
}
