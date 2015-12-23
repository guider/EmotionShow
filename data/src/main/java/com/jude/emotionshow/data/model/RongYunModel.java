package com.jude.emotionshow.data.model;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.jude.beam.model.AbsModel;
import com.jude.emotionshow.domain.entities.PersonBrief;
import com.jude.utils.JUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/7/8.
 * 融云第三方操作的API
 */
public class RongYunModel extends AbsModel {

    public static RongYunModel getInstance() {
        return getInstance(RongYunModel.class);
    }
    public BehaviorSubject<Integer> mNotifyBehaviorSubject = BehaviorSubject.create();
    public RongYunDelegate mDelegate;

    public String mCacheToken;
    public Handler hander = new Handler();
    public interface RongYunDelegate{
        void onPersonClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo);
    }

    public void setRongYunDelegate(RongYunDelegate delegate){
        this.mDelegate = delegate;
    }

    @Override
    protected void onAppCreate(Context ctx) {
        JUtils.Log("FUCK");
        UserModel.getInstance().getAccountUpdate().subscribe(user -> {
            JUtils.Log("I get");
            if (user != null) connectRongYun1(user.getRongYunToken());
            else loginOut();
        });
//        if (UserModel.getInstance().isLogin())
//            connectRongYun1(UserModel.getInstance().getCurAccount().getRongYunToken());
    }

    public void loginOut(){
        connectRongYun1("");
    }

    public Subscription registerNotifyCount(Action1<Integer> notify){
        return mNotifyBehaviorSubject.subscribe(notify);
    }

    public void connectRongYun1(String token){

        if (TextUtils.isEmpty(token)){
            JUtils.Log("TOKEN is Empty，Fuck！");
            return;
        }
        if (mCacheToken!=null&&mCacheToken.equals(token)){
            JUtils.Log("TOKEN NO Change");
            return;
        }
        mCacheToken = token;
        JUtils.Log("RongYunToken "+token);
        final String finalToken = token;
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                JUtils.Log("融云Token失效");
                //hander.post(()->JUtils.ToastLong("融云Token失效"+ finalToken));

            }

            @Override
            public void onSuccess(String s) {
                JUtils.Log("融云连接成功");
                //hander.post(()->JUtils.ToastLong("融云连接成功"+finalToken));
                setRongYun();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                //hander.post(()->JUtils.ToastLong("融云连接失败" + errorCode.getValue() + ":" + errorCode.getMessage()+ " "+finalToken));
                JUtils.Log("融云连接失败：" + errorCode.getValue() + ":" + errorCode.getMessage());
            }
        });
    }

    public void setRongYun(){
        JUtils.Log("setRongYun");
        try {
            RongIM.setUserInfoProvider(userId -> {
                JUtils.Log("I wanna "+userId);
                PersonBrief p;
                try{
                    p = UserModel.getInstance().getUserBrief(userId);
                    JUtils.Log("I get "+p.getId()+":"+p.getName());
                }catch (Exception e){
                    return null;
                }
                return new UserInfo(p.getId()+"", p.getName(), Uri.parse(ImageModel.getInstance().getSmallImage(p.getAvatar())));
            }, true);

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message message, int i) {
//                    if (JUtils.getSharedPreference().getBoolean(MsgSetActivity.CHAT_NOTIFY,true)){
//                        return false;//交给融云处理
//                    }
                    return true;//自己处理，不处理
                }
            });
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new RongIM.OnReceiveUnreadCountChangedListener() {
                @Override
                public void onMessageIncreased(int i) {
                    mNotifyBehaviorSubject.onNext(i);
                }
            }, Conversation.ConversationType.PRIVATE);

            RongIM.getInstance().setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                mDelegate.onPersonClick(context, conversationType, userInfo);
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });
        } catch (Exception e) {
            JUtils.Log("融云出错");
        }
    }


    public void updateRongYunPersonBrief(PersonBrief p){
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(p.getId()+"",p.getName(), Uri.parse(p.getAvatar())));
    }

    public void chatPerson(Context ctx,String id,String title){
//        Intent i = new Intent(ctx, ChatActivity.class);
//        i.putExtra("id",id);
//        i.putExtra("title",title);
//        i.putExtra("type", Conversation.ConversationType.PRIVATE.getName().toLowerCase());
//        ctx.startActivity(i);
        RongIM.getInstance().startPrivateChat(ctx, id, title);
    }

    public void chatGroup(Context ctx,String id,String title){
//        Intent i = new Intent(ctx, ChatActivity.class);
//        i.putExtra("id",id);
//        i.putExtra("title",title);
//        i.putExtra("type", Conversation.ConversationType.GROUP.getName().toLowerCase());
//        ctx.startActivity(i);
        RongIM.getInstance().startGroupChat(ctx,id,title);
    }

    public void chatList(Context ctx){
        RongIM.getInstance().startConversationList(ctx);
        //ctx.startActivity(new Intent(ctx, ChatListActivity.class));
    }
}
