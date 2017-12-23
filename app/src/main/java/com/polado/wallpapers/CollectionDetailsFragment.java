package com.polado.wallpapers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.polado.wallpapers.Model.Collection;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.rest.UnsplashApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class CollectionDetailsFragment extends Fragment implements AdapterView.OnItemClickListener {
    Collection collection;

    ImageView profilePic;

    ImageView errorMsg;

    ProgressBar progressBar;

    TextView title, creator;

    RecyclerView recyclerView;

    PhotosAdapter photosAdapter;

    int numberOfPages = 0, perPage = 10;

    AdapterView.OnItemClickListener onItemClickListener;

    public CollectionDetailsFragment() {
        // Required empty public constructor
    }

    public static CollectionDetailsFragment newInstance() {
        CollectionDetailsFragment fragment = new CollectionDetailsFragment();
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);
        recyclerView.startAnimation(slide_down);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collection_details, container, false);

        Bundle bundle = getArguments();
        String transitionName = "";
        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            collection = bundle.getParcelable("COLLECTION");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.collection_details_rv);

        slideUp();

        errorMsg = (ImageView) view.findViewById(R.id.collection_details_error_msg_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.collection_details_pb);

        profilePic = (ImageView) view.findViewById(R.id.collection_details_creator_iv);
        profilePic.setTransitionName(transitionName);

        Picasso.with(getContext()).load(collection.getUser().getProfileImage().getMedium())
                .transform(new CropCircleTransformation()).into(profilePic);

        title = (TextView) view.findViewById(R.id.collection_details_title_tv);
        title.setText(collection.getTitle());
        creator = (TextView) view.findViewById(R.id.collection_details_creator_tv);
        creator.setText(collection.getUser().getName());

        onItemClickListener = this;

        getCollectionPhotos();

        return view;
    }

    private void getCollectionPhotos() {
        new UnsplashApi().getCollectionPhotos(collection.getId(), 1, perPage, new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
                progressBar.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);

                photosAdapter = new PhotosAdapter(getContext(), photos, onItemClickListener);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(photosAdapter);

                numberOfPages++;
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.v("Error", error);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void slideUp() {
        Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        recyclerView.startAnimation(slide_up);
    }
}
