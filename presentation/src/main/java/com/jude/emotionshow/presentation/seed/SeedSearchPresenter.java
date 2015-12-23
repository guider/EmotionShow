package com.jude.emotionshow.presentation.seed;

import com.jude.beam.expansion.list.BeamListFragmentPresenter;
import com.jude.emotionshow.data.model.SeedModel;
import com.jude.emotionshow.domain.entities.Seed;

/**
 * Created by zhuchenxi on 15/11/23.
 */
public class SeedSearchPresenter extends BeamListFragmentPresenter<SeedSearchFragment,Seed> {


    public void search(String query){
        SeedModel.getInstance().searchSeed(query).unsafeSubscribe(getRefreshSubscriber());
    }
}
