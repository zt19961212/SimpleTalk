package com.william.flow.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.william.flow.model.db.AppDatabase;


/**
 * 群成员对应的用户的简单信息表
 *
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId; // id
    @Column
    public String name; // 用户名
    @Column
    public String alias; // 别名
    @Column
    public String portrait; // 头像
}
