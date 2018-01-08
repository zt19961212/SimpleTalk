package com.william.flow.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.william.flow.utils.DiffUiDataCallback;


/**
 *
 * 是数据库框架DbFlow中的基础类
 * 同时定义我们需要的方法
 *
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
