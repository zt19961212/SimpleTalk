package com.william.flow.data.apply;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.william.flow.data.BaseDbRepository;
import com.william.flow.model.db.Apply;
import com.william.flow.model.db.Apply_Table;

import java.util.List;

/**
 * 用户申请数据仓库
 */

public class ApplyRepository extends BaseDbRepository<Apply>
implements ApplyDataSource{
    @Override
    public void load(SucceedCallback<List<Apply>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Apply.class)
                .orderBy(Apply_Table.createTime,false)
                .limit(20)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Apply apply) {
        return true;
    }
}
