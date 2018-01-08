package com.william.flow.data.message;


import com.william.common.flow.data.DbDataSource;
import com.william.flow.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository, MessageGroupRepository
 * 关注的对象是Message表
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
