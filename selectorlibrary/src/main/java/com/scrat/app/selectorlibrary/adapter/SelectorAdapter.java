package com.scrat.app.selectorlibrary.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scrat.app.selectorlibrary.R;
import com.scrat.app.selectorlibrary.model.ISelectImageItem;
import com.scrat.app.selectorlibrary.model.Img;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yixuanxuan on 16/10/12.
 */

public class SelectorAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<ISelectImageItem> mData;
    private OnItemClickListener mListener;

    public SelectorAdapter(@NonNull OnItemClickListener listener) {
        mData = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        ISelectImageItem item = getItem(pos);
        if (item == null)
            return;

        final ImageView imageView = holder.getView(R.id.iv_img);
        Glide.with(imageView.getContext()).load(item.getImgPath()).centerCrop().into(imageView);
        if (item.isChecked()) {
            holder.setVisibility(R.id.iv_check, true);
            holder.setVisibility(R.id.v_shadown, true);
            ViewCompat.animate(holder.getRootView()).setDuration(300L).scaleX(0.9f).scaleY(0.9f).start();
        } else {
            holder.setVisibility(R.id.iv_check, false);
            holder.setVisibility(R.id.v_shadown, false);
            ViewCompat.animate(holder.getRootView()).setDuration(300L).scaleX(1.0f).scaleY(1.0f).start();
        }
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ISelectImageItem item = getItem(pos);
                if (item == null)
                    return;

                int effect = mListener.onItemClick(item, pos);
                if (effect == -1)
                    return;

                if (item.isChecked()) {
                    ViewCompat.animate(v).setDuration(300L).scaleX(1.0f).scaleY(1.0f).start();
                    holder.setVisibility(R.id.iv_check, false);
                    holder.setVisibility(R.id.v_shadown, false);
                } else {
                    ViewCompat.animate(v).setDuration(300L).scaleX(0.9f).scaleY(0.9f).start();
                    holder.setVisibility(R.id.iv_check, true);
                    holder.setVisibility(R.id.v_shadown, true);
                    Animation alphaAnimation = new AlphaAnimation(1f, 0f);
                    alphaAnimation.setDuration(500L);
                    holder.setText(R.id.tv_num, String.valueOf(effect));
                    holder.getView(R.id.tv_num).startAnimation(alphaAnimation);
                }
                item.setChecked(!item.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private ISelectImageItem getItem(int position) {
        if (position >= mData.size())
            return null;

        return mData.get(position);
    }

    public void replaceDatas(List<Img> data) {
        mData.clear();

        if (data != null && !data.isEmpty()) {
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }

    public void replaceCheckDatas(List<String> paths, List<Integer> posList) {
        Set<String> set = new HashSet<>(paths);
        Integer i = 0;
        for (ISelectImageItem img : mData) {
            boolean isChecked = set.contains(img.getImgPath());
            img.setChecked(isChecked);

            if (isChecked && !posList.contains(i)) {
                posList.add(i);
            } else if (!isChecked && posList.contains(i)) {
                posList.remove(i);
            }
            i ++;
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        int onItemClick(ISelectImageItem item, int pos);
    }

    public ArrayList<String> getPathByPosList(List<Integer> posList) {
        ArrayList<String> paths = new ArrayList<>();
        if (posList == null || posList.isEmpty())
            return paths;

        for (Integer pos : posList) {
            ISelectImageItem item = getItem(pos);
            if (item != null) {
                paths.add(item.getImgPath());
            }
        }
        return paths;
    }

}
