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

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.polado.wallpapers.Model.Collection;
import com.polado.wallpapers.rest.UnsplashApi;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionsFragment extends Fragment implements AdapterView.OnItemClickListener {
    CollectionsAdapter collectionsAdapter;
    RecyclerView recyclerView;

    ImageView errorMsgIV;

    ProgressBar progressBar;

    SwipyRefreshLayout swipyRefreshLayout;

    ArrayList<Collection> collectionsList = null;

    int numberOfPages = 0, perPage = 5;

    public static CollectionsFragment newInstance() {
        Log.i("CollectionsFragment", "newInstance");
        return new CollectionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CollectionsFragment", "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("NewFragment", "onSaveInstanceState");
        Log.i("collectionspre", collectionsList.toString());
        outState.putParcelableArrayList("collections", collectionsList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            collectionsList = savedInstanceState.getParcelableArrayList("photos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        boolean t;
        t = collectionsList != null;
        Log.i("collection frag", "" + t);
        Log.i("CollectionFragment", "onCreateView");

        final View view = inflater.inflate(R.layout.fragment_collections, container, false);

        final AdapterView.OnItemClickListener onItemClickListener = this;

        errorMsgIV = (ImageView) view.findViewById(R.id.collections_error_msg_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.collections_pb);

        if (collectionsList == null) {
            progressBar.setVisibility(View.VISIBLE);

            new UnsplashApi().getFeaturedCollections(1, perPage, new UnsplashApi.OnCollectionsLoadedListener() {
                @Override
                public void onLoaded(ArrayList<Collection> collections) {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorMsgIV.setVisibility(View.INVISIBLE);

                    collectionsList = collections;

                    collectionsList = collections;

                    collectionsAdapter = new CollectionsAdapter(getContext(), collectionsList, onItemClickListener);
                    recyclerView = (RecyclerView) view.findViewById(R.id.collections_images_rv);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(collectionsAdapter);

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

            collectionsAdapter = new CollectionsAdapter(getContext(), collectionsList, onItemClickListener);
            recyclerView = (RecyclerView) view.findViewById(R.id.collections_images_rv);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(collectionsAdapter);

        }

        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.collections_swipy_refresh);

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
//                    refresh();
                } else {
//                    loadMore();
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //you can get the clicked item from the adapter using its position

        //you can also find out which view was clicked
        switch (view.getId()) {
            case R.id.collection_home_creator_iv:
                makeTransition((ImageView) view, position);
                Toast.makeText(getContext(), " image " + position, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getContext(), " itemView " + position, Toast.LENGTH_SHORT).show();
        }
    }

    public void makeTransition(ImageView view, int position) {
        CollectionDetailsFragment detailsFragment = new CollectionDetailsFragment();

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
        bundle.putParcelable("COLLECTION", collectionsList.get(position));

        detailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .addSharedElement(view, imageTransitionName)
                .hide(this)
                .addToBackStack("collection")
                .replace(R.id.contentContainer, detailsFragment, "CollectionDetails")
                .commit();
    }
}
