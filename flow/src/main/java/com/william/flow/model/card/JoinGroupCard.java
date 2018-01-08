package com.william.flow.model.card;

/**
 * Created by hasee on 2018/1/5.
 */

public class JoinGroupCard {

    private ApplyCard applyCard;
    private GroupMemberCard memberCard;

    public ApplyCard getApplyCard() {
        return applyCard;
    }

    public void setApplyCard(ApplyCard applyCard) {
        this.applyCard = applyCard;
    }

    public GroupMemberCard getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(GroupMemberCard memberCard) {
        this.memberCard = memberCard;
    }
}
