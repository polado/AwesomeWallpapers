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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.rest.UnsplashApi;

public class TrendingFragment extends Fragment implements AdapterView.OnItemClickListener {
    ImagesAdapter imagesAdapter;
    RecyclerView recyclerView;

    ImageView errorMsgIV;

    ProgressBar progressBar;

    SwipyRefreshLayout swipyRefreshLayout;

    ArrayList<Photo> photosList = null;

    int numberOfPages = 0, perPage = 10;

    public static TrendingFragment newInstance() {
        return new TrendingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        final View view = inflater.inflate(R.layout.fragment_trending, container, false);

        final AdapterView.OnItemClickListener onItemClickListener = this;

        errorMsgIV = (ImageView) view.findViewById(R.id.error_msg_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.item_one_pb);

        if (photosList == null) {
            progressBar.setVisibility(View.VISIBLE);

            UnsplashApi unsplashApi = new UnsplashApi();
            unsplashApi.getCuratedPhotosList(1, perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
                @Override
                public void onLoaded(ArrayList<com.polado.wallpapers.Model.Photo> photos) {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorMsgIV.setVisibility(View.INVISIBLE);

                    photosList = photos;

                    imagesAdapter = new ImagesAdapter(getContext(), photosList, onItemClickListener);
                    recyclerView = (RecyclerView) view.findViewById(R.id.images_rv);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(imagesAdapter);

                    numberOfPages++;
                }

                @Override
                public void onFailure(String error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorMsgIV.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.v("Error", error);
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            errorMsgIV.setVisibility(View.INVISIBLE);
            imagesAdapter = new ImagesAdapter(getContext(), photosList, onItemClickListener);
            recyclerView = (RecyclerView) view.findViewById(R.id.images_rv);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(imagesAdapter);
        }

        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.new_swipy_refresh);

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

        UnsplashApi unsplashApi = new UnsplashApi();
        unsplashApi.getCuratedPhotosList(1, perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                errorMsgIV.setVisibility(View.INVISIBLE);

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

                imagesAdapter.notifyDataSetChanged();
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

        UnsplashApi unsplashApi = new UnsplashApi();
        unsplashApi.getCuratedPhotosList((numberOfPages + 1), perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<com.polado.wallpapers.Model.Photo> photos) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                errorMsgIV.setVisibility(View.INVISIBLE);

                photosList.addAll(photos);

                Log.d("loadmore", photos.size() + " " + photosList.size());

                imagesAdapter.notifyDataSetChanged();

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //you can get the clicked item from the adapter using its position

        //you can also find out which view was clicked
        switch (view.getId()) {
            case R.id.image_home_iv:
                makeTransation((ImageView) view, position);
                Toast.makeText(getContext(), " image " + position, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), " itemView " + position, Toast.LENGTH_SHORT).show();
        }
    }

    public void makeTransation(ImageView view, int position) {
        ImageDetailsFragment detailsFragment = new ImageDetailsFragment();

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
        bundle.putString("TRANS_NAME", imageTransitionName);
        bundle.putParcelable("IMAGE", photosList.get(position));

        detailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .addSharedElement(view, imageTransitionName)
                .addToBackStack("trending")
                .replace(R.id.contentContainer, detailsFragment, "ImageDetails")
                .commit();
    }
}
