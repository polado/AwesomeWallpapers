package com.polado.wallpapers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by PolaDo on 10/19/2017.
 */

public class ImagesAdapter2 extends RecyclerView.Adapter<ImagesAdapter2.ViewHolder> {

    Context context;

//    ArrayList<String> strings;
//    ArrayList<Drawable> images;
    ArrayList<com.polado.wallpapers.Model.Photo> photos;

    Boolean fav = false;

    private AdapterView.OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton favBtn;
        TextView numOfFavs;
        TextView creator;
        ImageView creatorProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_home_iv);
            favBtn = (ImageButton) itemView.findViewById(R.id.fav_btn);
            numOfFavs = (TextView) itemView.findViewById(R.id.image_number_of_favs);
            creator = (TextView) itemView.findViewById(R.id.image_home_creator_tv);
            creatorProfileImage = (ImageView) itemView.findViewById(R.id.image_home_creator_iv);
        }
    }

    public ImagesAdapter2(Context context, ArrayList<com.polado.wallpapers.Model.Photo> photos) {
        this.context = context;
//        this.strings = strings;

        this.photos = photos;

//        images = new ArrayList<>();
//        images.add(context.getResources().getDrawable(R.drawable.i01));
//        images.add(context.getResources().getDrawable(R.drawable.i02));
//        images.add(context.getResources().getDrawable(R.drawable.i03));
//        images.add(context.getResources().getDrawable(R.drawable.i04));
//        images.add(context.getResources().getDrawable(R.drawable.i05));

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Log.i("onBindViewHolder", strings.get(position));

        final com.polado.wallpapers.Model.Photo photo = photos.get(position);
        holder.numOfFavs.setText(String.valueOf(photo.getLikes()));
        holder.creator.setText(photo.getUser().getName());

//        holder.imageView.getLayoutParams().height = photo.getHeight();
//        holder.imageView.getLayoutParams().width = photo.getWidth();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor(photo.getColor()));

        Picasso.with(context).load(photo.getUrls().getRegular()).placeholder(gradientDrawable).into(holder.imageView);
        Picasso.with(context).load(photo.getUser().getProfileImage().getMedium())
                .transform(new CropCircleTransformation()).into(holder.creatorProfileImage);
//
//        fav = photo.getLikedByUser();

//        holder.imageView.setImageDrawable(images.get(position));

//        holder.imageView.setTransitionName("trans_image" + position);
//
//        holder.favBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
////                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 10);
////                scaleUp.setInterpolator(interpolator);
//
//                Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
//                scaleDown.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        if (fav)
//                            holder.favBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
//                        else
//                            holder.favBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
//                        holder.favBtn.startAnimation(scaleUp);
//                        fav = !fav;
//                        photo.setLikedByUser(!photo.getLikedByUser());
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//                holder.favBtn.startAnimation(scaleDown);
//                Toast.makeText(context, "fav this " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return photos .size();
    }
}
