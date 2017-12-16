package com.polado.wallpapers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Photo;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by PolaDo on 10/19/2017.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    Context context;

    //    ArrayList<String> strings;
//    ArrayList<Drawable> images;
    ArrayList<Photo> photos;

    Boolean fav = false;

    private AdapterView.OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(context, view.getId()+" Item click nr: "+getLayoutPosition(), Toast.LENGTH_SHORT).show();

            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getLayoutPosition(), view.getId());
        }
    }

    public ImagesAdapter(Context context, ArrayList<Photo> photos, AdapterView.OnItemClickListener onItemClickListener) {
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

        final Photo photo = photos.get(position);
//        if (photo.getExif() == null)
//            Log.i("onBindViewHolder", "null");
//        else
//            Log.i("onBindViewHolder", "not null");


        holder.numOfFavs.setText(String.valueOf(photo.getLikes()));
        holder.creator.setText(photo.getUser().getName());

        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth(),
                screenHeight = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

        int imageHeight = screenWidth * photo.getHeight() / photo.getWidth();

//        Log.i("width", screenWidth + " " + screenHeight + " " + imageHeight);

        holder.imageView.getLayoutParams().height = imageHeight;

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor(photo.getColor()));

        Picasso.with(context).load(photo.getUrls().getRegular()).placeholder(gradientDrawable).into(holder.imageView);
        Picasso.with(context).load(photo.getUser().getProfileImage().getMedium())
                .transform(new CropCircleTransformation()).into(holder.creatorProfileImage);

        holder.imageView.setTransitionName("trans_image" + position);

        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.3, 10);
//                scaleUp.setInterpolator(interpolator);

                Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down);
                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (fav)
                            holder.favBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                        else
                            holder.favBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                        holder.favBtn.startAnimation(scaleUp);
                        fav = !fav;
//                        photo.setLikedByUser(!photo.getLikedByUser());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                holder.favBtn.startAnimation(scaleDown);
                Toast.makeText(context, "fav this " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
