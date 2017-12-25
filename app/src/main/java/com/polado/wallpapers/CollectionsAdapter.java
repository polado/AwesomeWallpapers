package com.polado.wallpapers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.polado.wallpapers.Model.Collection;
import com.polado.wallpapers.utils.PhotoDimensions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by pola on 21/12/17.
 */

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ViewHolder> {

    private Context context;

    private ArrayList<Collection> collections;

    private AdapterView.OnItemClickListener onItemClickListener;

    public CollectionsAdapter(Context context, ArrayList<Collection> collections, AdapterView.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.collections = collections;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Collection collection = collections.get(position);

        holder.numOfPics.setText(String.valueOf(collection.getTotalPhotos()));
        holder.creator.setText(collection.getUser().getName());
        holder.title.setText(collection.getTitle());

        holder.imageView.getLayoutParams().height
                = new PhotoDimensions(context, collection.getCoverPhoto()).getHeight();

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor(collection.getCoverPhoto().getColor()));

        Picasso.with(context).load(collection.getCoverPhoto().getUrls().getRegular()).placeholder(gradientDrawable).into(holder.imageView);
        Picasso.with(context).load(collection.getUser().getProfileImage().getMedium())
                .transform(new CropCircleTransformation()).into(holder.creatorProfileImage);

        holder.creatorProfileImage.setTransitionName("trans_image" + position);

    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, creatorProfileImage;
        TextView numOfPics, creator, title;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.collection_home_iv);
            creatorProfileImage = (ImageView) itemView.findViewById(R.id.collection_home_creator_iv);
            numOfPics = (TextView) itemView.findViewById(R.id.collection_number_of_pics);
            creator = (TextView) itemView.findViewById(R.id.collection_home_creator_tv);
            title = (TextView) itemView.findViewById(R.id.collection_home_title_tv);
            cardView = (CardView) itemView.findViewById(R.id.collection_home_cv);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(null, creatorProfileImage, getLayoutPosition(), creatorProfileImage.getId());
        }
    }
}
