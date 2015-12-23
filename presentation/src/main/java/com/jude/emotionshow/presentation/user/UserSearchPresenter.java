package com.jude.emotionshow.presentation.user;

import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListFragmentPresenter;
import com.jude.emotionshow.data.model.UserModel;
import com.jude.emotionshow.domain.entities.PersonBrief;

/**
 * Created by zhuchenxi on 15/11/23.
 */
public class UserSearchPresenter extends BeamListFragmentPresenter<UserSearchFragment,PersonBrief> {

    public void search(String text){
        UserModel.getInstance().searchUser(text).unsafeSubscribe(getRefreshSubscriber());
    }
}
