package com.william.flow.data.apply;

import com.william.flow.model.card.ApplyCard;

/**
 * 用户申请中心
 */

public interface ApplyCenter {
    void dispatch(ApplyCard ... applyCards);
}
