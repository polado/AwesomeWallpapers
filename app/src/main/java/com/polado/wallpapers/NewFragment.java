package com.polado.wallpapers;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.rest.UnsplashApi;

import java.util.ArrayList;


public class NewFragment extends Fragment implements AdapterView.OnItemClickListener {
    public CardView toolbarCV, searchBarCV;
    PhotosAdapter photosAdapter;
    RecyclerView recyclerView;
    ImageView errorMsg;
    ProgressBar progressBar;
    SwipyRefreshLayout swipyRefreshLayout;
    ArrayList<Photo> photosList = null, adapterPhotosList = null;
    int numberOfPages = 0, perPage = 10;

    AdapterView.OnItemClickListener onItemClickListener;

    private Toolbar toolbar;
    private ImageButton searchBtn, navDrawerBtn, cancelSearchBtn;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    public static NewFragment newInstance() {
        Log.i("NewFragment", "newInstance");
        return new NewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("NewFragment", "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("NewFragment", "onSaveInstanceState");
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        appbarInitialization(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        boolean t;
        t = photosList != null;
        Log.i("new frag", "" + t);
        Log.i("NewFragment", "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_new, container, false);

        view.post(new Runnable() {
            @Override
            public void run() {
                appbarInitialization(view);
            }
        });

        onItemClickListener = this;

        errorMsg = (ImageView) view.findViewById(R.id.error_msg_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (photosList == null) {
            progressBar.setVisibility(View.VISIBLE);

            new UnsplashApi().getPhotosList(1, perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
                @Override
                public void onLoaded(ArrayList<com.polado.wallpapers.Model.Photo> photos) {
                    progressBar.setVisibility(View.INVISIBLE);
                    errorMsg.setVisibility(View.INVISIBLE);

                    photosList = photos;
//                    adapterPhotosList = photos;

                    photosAdapter = new PhotosAdapter(getContext(), photosList, onItemClickListener);
                    recyclerView = (RecyclerView) view.findViewById(R.id.new_images_rv);

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
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            errorMsg.setVisibility(View.INVISIBLE);

            photosAdapter = new PhotosAdapter(getContext(), photosList, onItemClickListener);
            recyclerView = (RecyclerView) view.findViewById(R.id.new_images_rv);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(photosAdapter);
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

    public void scroll() {
        recyclerView.smoothScrollToPosition(0);
    }

    void refresh() {
        numberOfPages = 0;

        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();

        new UnsplashApi().getPhotosList(1, perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
                swipyRefreshLayout.setRefreshing(false);

                progressBar.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);

                photosList = photos;
                photosAdapter = new PhotosAdapter(getContext(), photosList, onItemClickListener);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(photosAdapter);

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

//    void refresh() {
//        Toast.makeText(getContext(), "Top", Toast.LENGTH_SHORT).show();
//
//        new UnsplashApi().getPhotosList(1, perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
//            @Override
//            public void onLoaded(ArrayList<Photo> photos) {
//                swipyRefreshLayout.setRefreshing(false);
//
//                progressBar.setVisibility(View.INVISIBLE);
//                errorMsg.setVisibility(View.INVISIBLE);
//
//                if (Objects.equals(photosList.get(0).getPhotoID(), photos.get(0).getPhotoID())) {
//
//                    Log.i("refresh", "none");
//                    return;
//                } else if (photos.contains(photosList.get(0))) {
//                    int mutualIndex = photos.indexOf(photosList.get(0));
//                    ArrayList<Photo> list = photosList;
//                    photosList.clear();
//                    for (int i = 0; i <= mutualIndex; i++) {
//                        photosList.add(photos.get(i));
//                    }
//                    photosList.addAll(list);
//                    Log.i("refresh", "contains");
//                } else {
//                    ArrayList<Photo> list = photosList;
//                    photosList.clear();
//                    photosList = photos;
//                    photosList.addAll(list);
//                    Log.i("refresh", "new");
//                }
//
//                photosAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String error) {
//                swipyRefreshLayout.setRefreshing(false);
//
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                Log.v("Error", error);
//            }
//        });
//
//    }

    void loadMore() {
        Toast.makeText(getContext(), "LoadMore " + numberOfPages, Toast.LENGTH_SHORT).show();

        new UnsplashApi().getPhotosList((numberOfPages + 1), perPage, "latest", new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //you can get the clicked item from the adapter using its position

        //you can also find out which view was clicked
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
                .addToBackStack("new")
                .replace(R.id.contentContainer, detailsFragment, "PhotoDetails")
                .commit();
    }

    private void appbarInitialization(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navDrawerBtn = (ImageButton) view.findViewById(R.id.nav_drawer_btn);

        toolbarCV = (CardView) view.findViewById(R.id.toolbar_cv);
        searchBarCV = (CardView) view.findViewById(R.id.search_bar_cv);
        searchBtn = (ImageButton) view.findViewById(R.id.search_btn);
        cancelSearchBtn = (ImageButton) view.findViewById(R.id.cancel_search_btn);

        navDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        cancelSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSearch();
            }
        });

        searchBarCV.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorNewLight));
    }

    public void openDrawer() {
        ((Home) getActivity()).openDrawer();
    }

    public void search() {
        circularRevealAnimation(searchBtn);
    }

    public void cancelSearch() {
        reverseCircularRevealAnimation(searchBtn);
    }

    public void circularRevealAnimation(View v) {
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;

        float radius = Math.max(searchBarCV.getWidth(), searchBarCV.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(searchBarCV, cx, cy, 0, radius)
                .setDuration(400);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                searchBarCV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                toolbarCV.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void reverseCircularRevealAnimation(View v) {
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;

        float radius = Math.max(toolbarCV.getWidth(), toolbarCV.getHeight());

        Animator animator = ViewAnimationUtils.createCircularReveal(searchBarCV, cx, cy, radius, 0)
                .setDuration(400);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                toolbarCV.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchBarCV.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
