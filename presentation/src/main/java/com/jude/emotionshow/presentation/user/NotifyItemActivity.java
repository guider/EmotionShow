package com.jude.emotionshow.presentation.user;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.emotionshow.R;
import com.jude.emotionshow.domain.entities.Notify;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 15/12/8.
 */
@RequiresPresenter(NotifyItemPresenter.class)
public class NotifyItemActivity extends BeamListActivity<NotifyItemPresenter, Notify> {


    @Bind(R.id.back_img)
    ImageView backImg;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.recycler)
    EasyRecyclerView recycler;
    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        back.setOnClickListener(v -> finish());
        title.setText(getIntent().getStringExtra("title"));
    }

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setRefreshAble(true).setLoadmoreAble(true);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_notify_item;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        switch (getPresenter().type){
            case "1":
                return new NotifyPraiseViewHolder(parent);
            case "2":
                return new NotifyCommentViewHolder(parent);
            case "3":
                return new NotifyFollowViewHolder(parent);
            case "4":
                return new NotifyInviteViewHolder(parent);
        }
        return new NotifyViewHolder(parent);
    }
}
