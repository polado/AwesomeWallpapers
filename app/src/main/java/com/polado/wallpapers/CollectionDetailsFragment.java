package com.polado.wallpapers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
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

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.polado.wallpapers.Model.Collection;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.rest.UnsplashApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class CollectionDetailsFragment extends Fragment implements AdapterView.OnItemClickListener {
    Collection collection;

    ImageView profilePic;

    ImageView errorMsg;

    ProgressBar progressBar;

    SwipyRefreshLayout swipyRefreshLayout;

    TextView title, creator;

    ArrayList<Photo> photosList;

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("CollectionDetailsFrag", "onSaveInstanceState");
        Log.i("photospre", photosList.toString());
        outState.putParcelableArrayList("photos", photosList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            photosList = savedInstanceState.getParcelableArrayList("photos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collection_details, container, false);

        Bundle bundle = getArguments();
        String transitionName = "";
        if (bundle != null) {
            transitionName = bundle.getString("COLLECTION_TRANS_NAME");
            collection = bundle.getParcelable("COLLECTION");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.collection_details_rv);

        errorMsg = (ImageView) view.findViewById(R.id.error_msg_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

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

        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.collection_details_swipy_refresh);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    refresh();
                } else {
                    loadMore();
                }
            }
        });

        return view;
    }

    void refresh() {
        Toast.makeText(getContext(), "Top", Toast.LENGTH_SHORT).show();

        new UnsplashApi().getCollectionPhotos(collection.getId(), 1, perPage, new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);

                if (Objects.equals(photosList.get(0).getPhotoID(), photos.get(0).getPhotoID())) {

                    Log.i("refresh", "none");
                    return;
                } else if (photos.contains(photosList.get(0))) {
                    int mutualIndex = photos.indexOf(photosList.get(0));
                    ArrayList<Photo> list = photosList;
                    photosList.clear();
                    for (int i = 0; i <= mutualIndex; i++) {
                        photosList.add(photos.get(i));
                    }
                    photosList.addAll(list);
                    Log.i("refresh", "contains");
                } else {
                    ArrayList<Photo> list = photosList;
                    photosList.clear();
                    photosList = photos;
                    photosList.addAll(list);
                    Log.i("refresh", "new");
                }

                photosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.v("Error", error);
            }
        });
    }

    void loadMore() {
        Toast.makeText(getContext(), "Bottom " + numberOfPages, Toast.LENGTH_SHORT).show();

        new UnsplashApi().getCollectionPhotos(collection.getId(), numberOfPages + 1, perPage, new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<com.polado.wallpapers.Model.Photo> photos) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);

                photosList.addAll(photos);

                Log.d("loadmore", photos.size() + " " + photosList.size());

                photosAdapter.notifyDataSetChanged();

                recyclerView.smoothScrollToPosition(numberOfPages * perPage);

                numberOfPages++;
            }

            @Override
            public void onFailure(String error) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.v("Error", error);
            }
        });
    }

    private void getCollectionPhotos() {
        if (photosList == null)
            new UnsplashApi().getCollectionPhotos(collection.getId(), 1, perPage, new UnsplashApi.OnPhotosLoadedListener() {
                @Override
                public void onLoaded(ArrayList<Photo> photos) {
                    slideUp();

                    progressBar.setVisibility(View.INVISIBLE);
                    errorMsg.setVisibility(View.INVISIBLE);

                    photosList = photos;

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
        else {
            progressBar.setVisibility(View.INVISIBLE);
            errorMsg.setVisibility(View.INVISIBLE);

            photosAdapter = new PhotosAdapter(getContext(), photosList, onItemClickListener);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(photosAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (view.getId()) {
            case R.id.image_home_iv:
                makeTransition((ImageView) view, position);
                Toast.makeText(getContext(), " image " + position, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), " itemView " + position, Toast.LENGTH_SHORT).show();
        }
    }

    public void makeTransition(ImageView view, int position) {
        PhotoDetailsFragment detailsFragment = new PhotoDetailsFragment();

        setSharedElementReturnTransition(TransitionInflater.from(
                getActivity()).inflateTransition(R.transition.change_image_trans));
        setExitTransition(TransitionInflater.from(
                getActivity()).inflateTransition(android.R.transition.fade));

        detailsFragment.setSharedElementEnterTransition(TransitionInflater.from(
                getActivity()).inflateTransition(R.transition.change_image_trans));
        detailsFragment.setEnterTransition(TransitionInflater.from(
                getActivity()).inflateTransition(android.R.transition.fade));

        String imageTransitionName = view.getTransitionName();

        Bundle bundle = new Bundle();
        bundle.putString("PHOTO_TRANS_NAME", imageTransitionName);
        bundle.putParcelable("PHOTO", photosList.get(position));

        detailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .addSharedElement(view, imageTransitionName)
                .hide(this)
                .addToBackStack("CollectionDetails")
                .replace(R.id.contentContainer, detailsFragment, "PhotoDetails")
                .commit();
    }

    public void slideUp() {
        Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        recyclerView.startAnimation(slide_up);
    }
}
