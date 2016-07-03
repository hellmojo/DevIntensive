package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private ImageView mAvatarView;
    private DataManager mDataManager;
    private int mCurretEditMode = 0;
    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolBar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;



    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserAbout;
    private List<EditText> mUserInfoViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    File mPhotoFile = null;
    private Uri mSelectedImage = null;


    /*Придумать и записать сюда javadoc
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstanse();
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.git_et);
        mUserAbout = (EditText) findViewById(R.id.about_et);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView)findViewById(R.id.user_photo_img);
        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserAbout);


        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        setupToolBar();
        setupDrawer();
        loadUserInfoValue();
        Picasso.with(this)
                .load(mDataManager.getmPreferensesManager().loadUserPhoto())
                    .placeholder(R.drawable.user_photo)
                    .into(mProfileImage);


        if (savedInstanceState == null) {

        } else {
            mCurretEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurretEditMode);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else if (mCurretEditMode == 1) {
            changeEditMode(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserInfoValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurretEditMode == 0) {
                    changeEditMode(1);
                    mCurretEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurretEditMode = 0;
                }

                break;
            case R.id.profile_placeholder:
                /// Сделать выбор фотос камеры из загрузка с галлереи
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurretEditMode);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);

        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        mAvatarView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar);
        RoundedBitmapDrawable rBD = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        rBD.setCornerRadius(Math.max(bitmap.getHeight(), bitmap.getWidth()) / 2.0f);
        mAvatarView.setImageDrawable(rBD);
    }

    /**
     *Получение результата из другой activity (фото из камеры)
     * @param requestCode
     * @param resultCode
     * @param data
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if(resultCode == RESULT_OK && data != null){
                    mSelectedImage = data.getData();

                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode == RESULT_OK && mPhotoFile != null){
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
        }
    }



    /**
     * Переключает режим редактирования профиля
     *
     * @param mode если 1 режим редактирования, если 0 режим просмотра
     */

    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
                mUserPhone.requestFocus();
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserInfoValue();
            }
        }
    }

    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getmPreferensesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }

    }

    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getmPreferensesManager().saveUserProfileData(userData);
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, "Выберите фото"), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();

            }
            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }else
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

                Snackbar.make(mCoordinatorLayout, "Необходимо дать разрешение", Snackbar.LENGTH_LONG)
                        .setAction("Разрешить", new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                openApplicationSettings();
                            }
                        }).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
        }
        if(grantResults[1] == PackageManager.PERMISSION_GRANTED){

        }
    }

    private void hideProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.GONE);
    }
    private void showProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }
    private void lockToolbar(){
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }
    private void unlockToolbar(){
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.photo_dialog_gallery),getString(R.string.photo_dialog_camera), getString(R.string.photo_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.change_profile_photo);
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem){
                            case 0:
                               // showSnackbar("gallery");
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                //showSnackbar("photo");
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialogInterface.cancel();
                                showSnackbar("cancel");
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName  = "JPEG_"+timeStamp+" ";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);

        mDataManager.getmPreferensesManager().SaveUserPhoto(selectedImage);
    }

    public void openApplicationSettings(){
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMITION_REQUEST_SETTINGS_CODE);
    }
}
