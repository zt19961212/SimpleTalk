package com.william.flow.data.apply;


import android.text.TextUtils;

import com.william.flow.data.helper.DbHelper;
import com.william.flow.model.card.ApplyCard;
import com.william.flow.model.db.Apply;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApplyDispatcher implements ApplyCenter{
    private static  ApplyDispatcher instance;
    private final Executor executor= Executors.newSingleThreadExecutor();
    public static ApplyCenter getInstance()
    {
        if(instance==null)
        {
            synchronized (ApplyDispatcher.class)
            {
                if(instance==null)
                    instance=new ApplyDispatcher();
            }
        }
        return instance;
    }
    @Override
    public void dispatch(ApplyCard... applyCards) {
executor.execute(new ApplycardHandler(applyCards));
    }
    private class ApplycardHandler implements Runnable
    {
        private final ApplyCard[] applyCards;
        ApplycardHandler(ApplyCard[] cards)
        {
            this.applyCards=cards;
        }
        @Override
        public void run() {
            List<Apply> applies=new ArrayList<>();
            for (ApplyCard applyCard : applyCards) {
                if(applyCard==null|| TextUtils.isEmpty(applyCard.getId()))
                    continue;
                applies.add(applyCard.build());
            }
            DbHelper.save(Apply.class,applies.toArray(new Apply[0]));
        }
    }
}
