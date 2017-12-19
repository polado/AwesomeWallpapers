package com.polado.wallpapers;

import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Home extends AppCompatActivity {
    private Toolbar toolbar;

    private CardView toolbarCV, searchBarCV;
    private ImageButton searchBtn;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;

    Snackbar snackbar;

    private Fragment newFragment, trendingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            newFragment = NewFragment.newInstance();
            trendingFragment = TrendingFragment.newInstance();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        toolbarCV = (CardView) findViewById(R.id.toolbar_cv);
        searchBarCV = (CardView) findViewById(R.id.search_bar_cv);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment selectedFragment = null;
                String tag = "";
                switch (tabId) {
                    case R.id.tab_new:
//                        selectedFragment = NewFragment.newInstance();
//                        tag = "new";
                        showNewFragment();
                        break;
                    case R.id.tab_trending:
//                        selectedFragment = TrendingFragment.newInstance();
//                        tag = "trending";
                        showTrendingFragment();
                        break;
                    case R.id.tab_collection:
//                        selectedFragment = NewFragment.newInstance();
//                        tag = "new";
                        showNewFragment();
                        break;
                }

//                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home_coordinator_layout);
                snackbar = Snackbar.make(findViewById(R.id.coordinator), "hiiiiii", Snackbar.LENGTH_SHORT);
//                snackbar.show();
            }
        });

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment imageDetails = fragmentManager.findFragmentByTag("ImageDetails");

                if (imageDetails != null && imageDetails.isVisible()) {
                    Log.i("HomeFragmentChange", "imageDetails");
                    toolbar.setVisibility(View.INVISIBLE);
                    bottomBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.i("HomeFragmentChange", "not imageDetails");
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void showNewFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_in_down);

        if (newFragment.isAdded())
            transaction.show(newFragment);
        else
            transaction.add(R.id.contentContainer, newFragment, "new");

        if (trendingFragment.isAdded())
            transaction.hide(trendingFragment);

        transaction.commit();
    }

    protected void showTrendingFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_in_down);

        if (trendingFragment.isAdded())
            transaction.show(trendingFragment);
        else
            transaction.add(R.id.contentContainer, trendingFragment, "trending");

        if (newFragment.isAdded())
            transaction.hide(newFragment);

        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // below line to be commented to prevent crash on nougat.
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
        //
        //super.onSaveInstanceState(outState);
    }

    public void openDrawer(View view) {
        drawer.openDrawer(navigationView);
    }

    public void search(View view) {
        circularRevealAnimation(searchBtn);
    }

    public void cancelSearch(View view) {
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