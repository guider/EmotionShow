package com.jude.emotionshow.presentation.seed;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.emotionshow.R;
import com.jude.emotionshow.data.model.ImageModel;
import com.jude.emotionshow.domain.entities.CategoryPreview;
import com.jude.emotionshow.domain.entities.Image;
import com.jude.emotionshow.domain.entities.Seed;
import com.jude.utils.JUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2015/11/19.
 */
public class CategoryItemView extends LinearLayout {


    @Bind(R.id.background)
    ImageView background;
    @Bind(R.id.title_zh)
    TextView titleZh;
    @Bind(R.id.title_en)
    TextView titleEn;
    @Bind(R.id.list)
    EasyRecyclerView list;

    Adapter adapter;

    public CategoryItemView(Context context) {
        super(context);
        init();
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.view_category_item, this);
        ButterKnife.bind(this, this);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list.setAdapter(adapter = new Adapter(getContext()));
    }

    public void setCategory(CategoryPreview categoryPreview){
        Picasso.with(getContext())
                .load(categoryPreview.getCategory().getAvatar())
                .into(background);
        titleEn.setText(categoryPreview.getCategory().getNameEn());
        titleZh.setText(categoryPreview.getCategory().getName());
        adapter.clear();

        if (categoryPreview.getData().size()>3)
            adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
                @Override
                public View onCreateView(ViewGroup parent) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.view_category_footer, parent, false);
                    view.setOnClickListener(v->{
                        Intent i = new Intent(getContext(),CategoryActivity.class);
                        i.putExtra("category", categoryPreview.getCategory());
                        getContext().startActivity(i);
                    });
                    return view;
                }

                @Override
                public void onBindView(View headerView) {

                }
            });
        adapter.addAll(categoryPreview.getData());

        background.setOnClickListener(v->{
            Intent i = new Intent(getContext(),CategoryActivity.class);
            i.putExtra("category", categoryPreview.getCategory());
            getContext().startActivity(i);
        });
    }

    private class Adapter extends RecyclerArrayAdapter<Seed> {
        public Adapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }
    }

    private class ViewHolder extends BaseViewHolder<Seed>{

        public ViewHolder(ViewGroup parent) {
            super(new ImageView(parent.getContext()));
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(JUtils.dip2px(84), JUtils.dip2px(76));
            itemView.setLayoutParams(new ViewGroup.LayoutParams(params));
            itemView.setPadding(0, 0, JUtils.dip2px(8), 0);
            ((ImageView) itemView).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        @Override
        public void setData(Seed data) {
            itemView.setOnClickListener(v -> {
                Intent i = new Intent(getContext(), SeedDetailActivity.class);
                i.putExtra("id",data.getId());
                getContext().startActivity(i);
            });
            Image image = ImageModel.getSmallImage(data.getPics().get(0));
            Picasso.with(getContext())
                    .load(image.getUrl())
                    .into((ImageView) itemView);
        }

    }
}
