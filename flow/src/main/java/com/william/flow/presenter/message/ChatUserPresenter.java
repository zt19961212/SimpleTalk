package com.william.flow.presenter.message;


import com.william.flow.data.helper.UserHelper;
import com.william.flow.data.message.MessageRepository;
import com.william.flow.model.db.Message;
import com.william.flow.model.db.User;

/**
 * 用户聊天逻辑
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);


    }

    @Override
    public void start() {
        super.start();

        // 从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
