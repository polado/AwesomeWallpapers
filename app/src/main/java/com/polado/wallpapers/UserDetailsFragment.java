package com.polado.wallpapers;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polado.wallpapers.Model.User;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class UserDetailsFragment extends Fragment {

    User user;

    ImageView userProfileImage;

    CardView userDetails;

    TextView userName, userLocation, userInfo;

    public UserDetailsFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        Bundle bundle = getArguments();
        String transitionName = "";
        if (bundle != null) {
            transitionName = bundle.getString("USER_TRANS_NAME");
            user = bundle.getParcelable("USER");
        }

        userName = (TextView) view.findViewById(R.id.user_details_name_tv);
        if (user.getName() != null)
            userName.setText(user.getName().trim());

        userLocation = (TextView) view.findViewById(R.id.user_details_location_tv);
        if (user.getLocation() != null)
            userLocation.setText(user.getLocation().trim());

        userInfo = (TextView) view.findViewById(R.id.user_details_info_tv);
        if (user.getBio() != null)
            userInfo.setText(user.getBio().trim());

        userDetails = (CardView) view.findViewById(R.id.user_details_cv);

        userProfileImage = (ImageView) view.findViewById(R.id.user_details_iv);
        userProfileImage.setTransitionName(transitionName);

        Picasso.with(getContext()).load(user.getProfileImage().getLarge())
                .transform(new CropCircleTransformation()).into(userProfileImage);

        Palette.generateAsync(((BitmapDrawable) userProfileImage.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {

//                int defaultColor = palette.getDominantColor(0);
//                int color = palette.getLightVibrantColor(defaultColor);
                int color = palette.getDominantColor(0);
                userDetails.setCardBackgroundColor(color);

                if (Color.red(color) + Color.green(color) + Color.blue(color) < 300) {
                    userName.setTextColor(Color.WHITE);
                    userLocation.setTextColor(Color.WHITE);
                    userInfo.setTextColor(Color.WHITE);
                } else {
                    userName.setTextColor(Color.BLACK);
                    userLocation.setTextColor(Color.BLACK);
                    userInfo.setTextColor(Color.BLACK);
                }
            }
        });

        return view;
    }
}
