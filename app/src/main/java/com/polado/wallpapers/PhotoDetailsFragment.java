package com.polado.wallpapers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.polado.wallpapers.Model.Category;
import com.polado.wallpapers.Model.DownloadLink;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.Model.PhotoStats;
import com.polado.wallpapers.rest.UnsplashApi;
import com.polado.wallpapers.utils.DownloadPhotoService;
import com.polado.wallpapers.utils.DownloadingNotification;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PhotoDetailsFragment extends Fragment {
    int imageID;

    boolean statsCheck = false, photoCheck = false;
    String unknown = "Unknown", photoFile = null;

    Photo photo, photoData;
    PhotoStats photoStats;

    LinearLayout linearLayout;

    ImageView imageView, paletteColor, creatorProfileImage;

    TextView creator, createdAt,
            views, likes, downloads,
            ratio, palette, location, camera, iso, exposure, aperture, focal,
            categories;

    ProgressBar progressBar;

    ProgressDialog progressDialog;

    Snackbar snackbar;

    View detailsLayout;

    DownloadingNotification notification;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("downloadPhoto", " onBitmapLoaded");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final File directory = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AwesomeWallpapers")); // path to /data/data/yourapp/app_imageDir
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    final File myImageFile = new File(directory, photoFile);

                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(myImageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("downloadPhoto", "image saved to >>>" + myImageFile.getAbsolutePath());

                    progressDialog.cancel();

                    startService(photoFile, imageID, false);
//                    notification.updateNotification(photoFile, imageID);
                    ((Home) getActivity()).snackbar.setText("Downloaded").show();
//                    snackbar.setText("Downloaded").show();

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("downloadPhoto", " onBitmapFailed");
            progressDialog.cancel();
//            snackbar.setText("Download Failed").show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d("downloadPhoto", " onPrepareLoad");
            progressDialog.show();

            startService(photoFile, imageID, true);
//            notification.createNotification(photoFile, imageID);
        }
    };

    public static PhotoDetailsFragment newInstance() {
        return new PhotoDetailsFragment();
    }

    private void startService(String msg, int id, boolean action) {
        Intent intent = new Intent(getContext(), DownloadPhotoService.class);
        intent.putExtra("MSG", msg);
        intent.putExtra("ID", id);
        intent.putExtra("ACTION", action);

        getContext().startService(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);
        linearLayout.startAnimation(slide_down);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_image_details, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        snackbar = Snackbar.make(view, "Photo Downloaded", Snackbar.LENGTH_SHORT);

        Bundle bundle = getArguments();
        String transitionName = "";
        if (bundle != null) {
            transitionName = bundle.getString("PHOTO_TRANS_NAME");
            photo = bundle.getParcelable("PHOTO");
        }

        linearLayout = (LinearLayout) view.findViewById(R.id.image_details_ll);

        slideUp();

        imageView = (ImageView) view.findViewById(R.id.image_details_iv);
        imageView.setTransitionName(transitionName);
        creatorProfileImage = (ImageView) view.findViewById(R.id.details_creator_profile_iv);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor(photo.getColor()));
        Picasso.with(getContext()).load(photo.getUrls().getRegular()).placeholder(gradientDrawable).into(imageView);
        Picasso.with(getContext()).load(photo.getUser().getProfileImage().getLarge())
                .transform(new CropCircleTransformation()).into(creatorProfileImage);

        creator = (TextView) view.findViewById(R.id.image_creator);
        createdAt = (TextView) view.findViewById(R.id.image_created_at);

        if (photo.getUser().getName() != null)
            creator.setText(photo.getUser().getName());
        else
            creator.setText(unknown);

        if (photo.getCreatedAt() != null)
            createdAt.setText(photo.getCreatedAt().split("T")[0]);
        else
            createdAt.setText(unknown);

        photoFile = creator.getText().toString().replace(" ", "_").trim()
                + "_"
                + createdAt.getText().toString().trim()
                + ".jpg";

        views = (TextView) view.findViewById(R.id.details_views_tv);
        likes = (TextView) view.findViewById(R.id.details_favs_tv);
        downloads = (TextView) view.findViewById(R.id.details_downloads_tv);

        ratio = (TextView) view.findViewById(R.id.details_ratio_tv);
        palette = (TextView) view.findViewById(R.id.details_palette_tv);
        location = (TextView) view.findViewById(R.id.details_location_tv);
        camera = (TextView) view.findViewById(R.id.details_camera_tv);
        iso = (TextView) view.findViewById(R.id.details_iso_tv);
        exposure = (TextView) view.findViewById(R.id.details_exposure_tv);
        aperture = (TextView) view.findViewById(R.id.details_aperture_tv);
        focal = (TextView) view.findViewById(R.id.details_focal_tv);

        categories = (TextView) view.findViewById(R.id.details_categories_tv);

        paletteColor = (ImageView) view.findViewById(R.id.details_palette_iv);

        progressBar = (ProgressBar) view.findViewById(R.id.image_details_pb);

        detailsLayout = view.findViewById(R.id.details_layout_include);


        Log.i("description", "msggg " + photo.getDescription());

        if (photoData == null)
            Log.i("photodata", "null");
        else Log.i("photodata", "not null");

        getPhotoData();
        getPhotoStats();

        ImageButton backBtn = (ImageButton) view.findViewById(R.id.image_details_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                } else {
                    Log.i("MainActivity", "nothing on backstack, calling super");
                }
            }
        });

        ImageButton likeBtn = (ImageButton) view.findViewById(R.id.image_details_like_btn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLike(photo.getPhotoID());
            }
        });

        ImageButton downloadBtn = (ImageButton) view.findViewById(R.id.image_details_download_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageID = photo.getLikes() * photo.getWidth() / photo.getHeight();
                notification = new DownloadingNotification(getContext());

                Log.i("downloadPhoto", "downloadBtn");
                if (checkPhotoExist()) {
                    Log.i("downloadPhoto", "if downloaded");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Photo Already Exists").setCancelable(true).show();
                } else {
                    Log.i("downloadPhoto", "else downloaded");
                    if (checkStoragePermission())
                        getDownloadLink();
                    else
                        Toast.makeText(getContext(), "need permission to write to storage", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    boolean checkPhotoExist() {

        Log.i("downloadPhoto", "check");
        File directory = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AwesomeWallpapers"));
        if (directory.exists()) {
            Log.i("downloadPhoto", "check1");
            File myPhoto = new File(directory.getAbsolutePath() + "/" + photoFile);
            if (myPhoto.exists()) {
                Log.i("downloadPhoto", "check2");
                return true;
            }
        }
        return false;
    }

    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void slideUp() {
        Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        linearLayout.startAnimation(slide_up);
    }

    public void setPhotoData() {

        String ratioString = photoData.getWidth() + " x " + photoData.getHeight();
        ratio.setText(ratioString);

        palette.setText(photoData.getColor());
        DrawableCompat.setTint(paletteColor.getDrawable(), Color.parseColor(photoData.getColor()));

        String locationString = "Unknown";
        if (photoData.getLocation() != null)
            locationString = photoData.getLocation().getCity() + ", " + photoData.getLocation().getCountry();
        location.setText(locationString);

        setExif(photoData);

        StringBuilder categoriesList = new StringBuilder("No Categories");
        if (photoData.getCategories() != null && photoData.getCategories().size() > 1) {
            categoriesList = new StringBuilder("Categories: ");
            Log.i("Categories", photoData.getCategories().size() + "");
            for (Category c : photoData.getCategories()) {
                categoriesList.append(c.getTitle()).append(" ");
                Log.i("Categories", c.getTitle());

            }
        }

        Log.i("PhotoDisc", "msg: " + photoData.getDescription());

        categories.setText(categoriesList.toString());
    }

    public void setExif(Photo photo) {

        if (photo.getExif() == null) {
            camera.setText(unknown);
            iso.setText(unknown);
            exposure.setText(unknown);
            aperture.setText(unknown);
            focal.setText(unknown);
        } else {
            if (photo.getExif().getModel() != null && photo.getExif().getMake() != null) {
                String make = photo.getExif().getMake(), model = photo.getExif().getModel();
                Log.i("camera", make + " " + model);
                if (model.toLowerCase().contains(make.toLowerCase()))
                    camera.setText(String.valueOf(model));
                else {
                    String cam = make + " " + model;
                    camera.setText(cam);
                }
            } else
                camera.setText(unknown);

            {
                if (photo.getExif().getIso() != null)
                    iso.setText(String.valueOf(photo.getExif().getIso()));
                else
                    iso.setText(unknown);
            }
            {
                if (photo.getExif().getExposureTime() != null)
                    exposure.setText(String.valueOf(photo.getExif().getExposureTime()));
                else
                    exposure.setText(unknown);
            }
            {
                if (photo.getExif().getAperture() != null)
                    aperture.setText(String.valueOf(photo.getExif().getAperture()));
                else
                    aperture.setText(unknown);
            }
            {
                if (photo.getExif().getFocalLength() != null)
                    focal.setText(String.valueOf(photo.getExif().getFocalLength()));
                else
                    focal.setText(unknown);
            }
        }
    }

    public void setPhotoStats() {
        if (photoStats.getDownloads() != null)
            downloads.setText(String.valueOf(photoStats.getDownloads().getTotal()));
        else
            downloads.setText(unknown);

        if (photoStats.getLikes() != null)
            likes.setText(String.valueOf(photoStats.getLikes().getTotal()));
        else
            likes.setText(unknown);

        if (photoStats.getDownloads() != null)
            views.setText(String.valueOf(photoStats.getViews().getTotal()));
        else
            views.setText(unknown);
    }

    public void getPhotoStats() {
        UnsplashApi unsplashApi = new UnsplashApi();
        unsplashApi.getPhotoStats(photo.getPhotoID(), new UnsplashApi.OnPhotoStatsLoadedListener() {
            @Override
            public void onLoaded(PhotoStats stats) {
                photoStats = stats;
                setPhotoStats();

                statsCheck = true;
                if (photoCheck) {
                    progressBar.setVisibility(View.INVISIBLE);
                    detailsLayout.setVisibility(View.VISIBLE);

                    statsCheck = false;
                    photoCheck = false;
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public void getPhotoData() {
        new UnsplashApi().getPhotoByID(photo.getPhotoID(), null, null, new UnsplashApi.OnPhotoLoadedListener() {
            @Override
            public void onLoaded(Photo photo) {
                photoData = photo;
                setPhotoData();

                photoCheck = true;
                if (statsCheck) {
                    progressBar.setVisibility(View.INVISIBLE);
                    detailsLayout.setVisibility(View.VISIBLE);

                    statsCheck = false;
                    photoCheck = false;
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public void getDownloadLink() {
        Log.d("downloadLink", "getDownloadLink " + photo.getPhotoID());
        new UnsplashApi().getPhotoDownloadLink(photo.getPhotoID(), new UnsplashApi.OnLinkLoadedListener() {

            @Override
            public void onLoaded(DownloadLink downloadLink) {
                Picasso.with(getContext()).load(downloadLink.getUrl()).into(target);
//                Picasso.with(getContext()).load(photo.getUrls().getRaw()).into(target);
                Log.d("downloadLink", downloadLink.getUrl());
            }

            @Override
            public void onFailure(String error) {
                Log.d("downloadLink", error);
            }
        });
    }

    private void setLike(String id) {

        Log.i("liked", "set Like");
        new UnsplashApi().setLike(id, new UnsplashApi.OnPhotoLikedListener() {
            @Override
            public void onLiked(String msg) {
                Log.i("liked", "msg " + msg);
            }

            @Override
            public void onFailure(String error) {
                Log.i("liked", "error " + error);
            }
        });
    }
}
