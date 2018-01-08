package com.william.flow.data.helper;


import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.william.flow.Flow;
import com.william.flow.model.api.RspModel;
import com.william.flow.model.card.ApplyCard;
import com.william.flow.model.db.Apply;
import com.william.flow.model.db.Apply_Table;
import com.william.flow.network.Network;
import com.william.flow.network.RemoteService;

import retrofit2.Response;

public class ApplyHelper {
    // 从本地查询一条申请
    public static Apply findFromLocal(String id) {
        return SQLite.select()
                .from(Apply.class)
                .where(Apply_Table.id.eq(id))
                .querySingle();
    }

    // 从网络查询一条申请
    public static Apply findFromNet(String id) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<ApplyCard>> response = remoteService.searchApply(id).execute();
            ApplyCard card = response.body().getResult();
            if (card != null) {
               Apply apply = card.build();
                // 数据库的存储并通知
                Flow.getApplyCenter().dispatch(card);
                return apply;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 搜索一条申请，优先本地缓存，
     * 没有用然后再从网络拉取
     */
    public static Apply search(String id) {
      Apply apply = findFromLocal(id);
        if (apply == null) {
            return findFromNet(id);
        }
        return apply;
    }

    /**
     * 搜索一条申请，优先网络查询
     * 没有用然后再从本地缓存拉取
     */
    public static Apply searchFirstOfNet(String id) {
       Apply apply= findFromNet(id);
        if (apply == null) {
            return findFromLocal(id);
        }
        return apply;
    }
}
