package com.polado.wallpapers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import Model.Category;
import Model.Photo;
import Model.PhotoStats;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import rest.UnsplashApi;
import utils.ImageTarget;

public class ImageDetailsFragment extends Fragment {
    int imageID;

    boolean statsCheck = false, photoCheck = false;
    String unknown = "Unknown";

    Photo photo, photoData;
    PhotoStats photoStats;

    LinearLayout linearLayout;

    ImageView imageView, paletteColor, creatorProfileImage;

    TextView creator, createdAt,
            views, likes, downloads,
            ratio, palette, location, camera, iso, exposure, aperture, focal,
            categories;

    ProgressBar progressBar;

    View detailsLayout;

    public static ImageDetailsFragment newInstance() {
        return new ImageDetailsFragment();
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
        Bundle bundle = getArguments();
        String transitionName = "";
        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            photo = bundle.getParcelable("IMAGE");
        }

        final View view = inflater.inflate(R.layout.fragment_image_details, container, false);

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
                Log.i("downlaod", "downloadBtn");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                final ImageTarget imageTarget = new ImageTarget(getContext().getContentResolver(),
                        "Awesome Wallpapers", "awallpaper");

                final Target mTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        Log.d("DEBUG", "onBitmapLoaded");
                        if (getContext().getContentResolver() != null) {
                            Log.i("DEBUG", "if");
                            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "awallpaper", "Awesome Wallpapers");
                        } else {
                            Log.i("DEBUG", "else");
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {
                        Log.d("DEBUG", "onBitmapFailed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {
                        Log.d("DEBUG", "onPrepareLoad");
                    }
                };

                Picasso.with(getContext()).load(photo.getUrls().getRaw())
//                        .into(saveImage(getContext(), "Awesome Wallpapers", "awallpaper"));
                        .into(mTarget);

//                    }
//                });
            }
        });
        return view;
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

        String categoriesList = "No Categories";
        if (photoData.getCategories() != null && photoData.getCategories().size() > 1) {
            categoriesList = "Categories: ";
            Log.i("Categories", photoData.getCategories().size() + "");
            for (Category c : photoData.getCategories()) {
                categoriesList += c.getTitle() + " ";
                Log.i("Categories", c.getTitle());

            }
        }
        categories.setText(categoriesList);
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
                else
                    camera.setText(make + " " + model);
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
        UnsplashApi unsplashApi = new UnsplashApi();
        unsplashApi.getPhotoByID(photo.getPhotoID(), null, null, new UnsplashApi.OnPhotoLoadedListener() {
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

    private Target saveImage(final Context context, final String imageDir, final String imageName) {
        Log.i("downlaod", "saveImage");
//        ContextWrapper contextWrapper = new ContextWrapper(context);
//        final File directory = contextWrapper.getDir(imageDir, Context.MODE_PRIVATE);

        return new Target() {
            private final WeakReference<ContentResolver> resolver = new WeakReference<>(context.getContentResolver());
            ;

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("downlaod", "onBitmapLoaded");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {

                ContentResolver r = resolver.get();
                if (r != null) {
                    Log.i("downlaod", "if");
                    MediaStore.Images.Media.insertImage(r, bitmap, imageName, imageDir);
                } else {
                    Log.i("downlaod", "else");
                }

//                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + imageName);
//                        try {
//                            file.createNewFile();
//                            FileOutputStream ostream = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
//                            ostream.flush();
//                            ostream.close();
//                        } catch (IOException e) {
//                            Log.e("IOException", e.getLocalizedMessage());
//                        }

//                        final File imageFile = new File(directory, imageName);
//                        FileOutputStream fileOutputStream = null;
//
//                        try {
//                            fileOutputStream = new FileOutputStream(imageFile);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                fileOutputStream.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("downlaod", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("downlaod", "onPrepareLoad");
            }
        };
    }

}
