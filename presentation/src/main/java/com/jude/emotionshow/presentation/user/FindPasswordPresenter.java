package com.jude.emotionshow.presentation.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jude.beam.bijection.Presenter;
import com.jude.emotionshow.data.model.UserModel;
import com.jude.emotionshow.data.server.ServiceResponse;
import com.jude.smssdk_mob.SMSManager;
import com.jude.utils.JUtils;


/**
 * Created by Mr.Jude on 2015/11/23.
 */
public class FindPasswordPresenter extends Presenter<FindPasswordActivity> {


    @Override
    protected void onCreate(FindPasswordActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
    }

    @Override
    protected void onCreateView(FindPasswordActivity view) {
        super.onCreateView(view);
        SMSManager.getInstance().registerTimeListener(getView());
    }

    public void checkTelAndSend(String number){
        if (!getView().requestPermission()){
            return;
        }
        SMSManager.getInstance().sendMessage(getView(), "86",number);
    }

    public void register(String number,String password,String code){
        getView().getExpansion().showProgressDialog("提交中");
        UserModel.getInstance().findPassword(number, code, password)
                .finallyDo(() -> getView().getExpansion().dismissProgressDialog())
                .subscribe(new ServiceResponse<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Intent i = new Intent();
                        i.putExtra("account", number);
                        i.putExtra("password", password);
                        getView().setResult(Activity.RESULT_OK, i);
                        getView().finish();
                        JUtils.Toast("修改成功");
                    }
                });
    }
}
