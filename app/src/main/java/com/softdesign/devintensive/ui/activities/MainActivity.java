package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.softdesign.devintensive.utils.NetworkStatusChecker;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private ImageView mAvatarView;
    private DataManager mDataManager;
    private int mCurretEditMode = 0;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolBar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;

    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;
    private ImageView mCallImg,mEmailImg,mVkImg,mGitImg;
    private TextView mUserValueRating, mUserValueCodeLines, mUserValueProjects;
    private EditText mUserPhone,mUserMail, mUserVk, mUserGit, mUserAbout;
    private List<TextView> mUserValueViews;
    private List<EditText> mUserFieldViews;
    private TextView mUserProfileText,mUserEmailText;

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
        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.git_et);
        mUserAbout = (EditText) findViewById(R.id.about_et);
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView)findViewById(R.id.user_photo_img);
        mCallImg = (ImageView) findViewById(R.id.call_img);
        mEmailImg = (ImageView) findViewById(R.id.email_img);
        mVkImg = (ImageView) findViewById(R.id.vk_img);
        mGitImg = (ImageView) findViewById(R.id.git_img);
        mUserValueRating = (TextView) findViewById(R.id.user_rating);
        mUserValueCodeLines = (TextView) findViewById(R.id.user_code_lines);
        mUserValueProjects = (TextView) findViewById(R.id.user_projects);


        ButterKnife.bind(this);
        mUserFieldViews = new ArrayList<>();
        mUserFieldViews.add(mUserPhone);
        mUserFieldViews.add(mUserMail);
        mUserFieldViews.add(mUserVk);
        mUserFieldViews.add(mUserGit);
        mUserFieldViews.add(mUserAbout);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        mUserProfileText = (TextView)findViewById(R.id.user_name_txt);
        mUserEmailText = (TextView)findViewById(R.id.user_email_txt);
        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mCallImg.setOnClickListener(this);
        mEmailImg.setOnClickListener(this);
        mVkImg.setOnClickListener(this);
        mGitImg.setOnClickListener(this);


        setupToolBar();
        setupDrawer();
        initData();
        Picasso.with(this)
                .load(mDataManager.getmPreferensesManager().loadUserPhoto())
                .placeholder(R.drawable.user_bg)
                .into(mProfileImage);


        if (savedInstanceState == null) {

        } else {
            mCurretEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurretEditMode);
        }

    }


    //Открытие drawer при нажатии на меню

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    //Свертывание drawer при нажатии back
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
        saveUserField();
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
                if (mCurretEditMode == 0 ) {
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
            case R.id.call_img:
                /// Позвонить по указанному в ET номеру
                profileCall(mUserFieldViews.get(0).getText().toString());
                break;
            case R.id.email_img:
                ///Отправить email по указанному адресу
                sendEmail(mUserFieldViews.get(1).getText().toString());
                break;
            case R.id.vk_img:
                ///Открыть указанный профиль
                openVkProfile(mUserFieldViews.get(2).getText().toString());
                break;
            case R.id.git_img:
                ///Открыть указанный профиль
                openGitProfile(mUserFieldViews.get(3).getText().toString());
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
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.team_menu:
                        Intent toUserList = new Intent(MainActivity.this, UserListActivity.class);
                        startActivity(toUserList);
                }
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
            for (EditText userValue : mUserFieldViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
                mUserPhone.requestFocus();//Ставит фокус на строку сотовый
                mUserPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserFieldViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

                saveUserField();
            }
        }
    }


    private void initData() {
                initUserField();
                initUserInfoValue();
                initContentValue();
                //initNavValue();
                initPhoto();
            }

    private void initUserField() {
        List<String> userFields = mDataManager.getmPreferensesManager().loadUserProfileData();
        for (int i = 0; i < userFields.size(); i++) {
            mUserFieldViews.get(i).setText(userFields.get(i));
        }
    }

        private void initUserInfoValue() {
            List<String> userData = mDataManager.getmPreferensesManager().loadUserProfileValue();
                    for (int i = 0; i < userData.size(); i++) {
                            mUserValueViews.get(i).setText(userData.get(i));
                        }
                }

               private void initContentValue() {
                   List<String> contentData = mDataManager.getmPreferensesManager().loadContentValue();
                   for (int i = 0; i < contentData.size(); i++) {
                       mUserFieldViews.get(i).setText(contentData.get(i));
                   }
               }

                private void initNavValue() {
                    List<String> navData = mDataManager.getmPreferensesManager().loadNavValue();
                    try {
                        mUserProfileText.setText(navData.get(0) + " " + navData.get(1));
                    } catch (Exception e) {
                        Log.d("M", e.getMessage());
                    }
                   // mUserEmailText.setText(mDataManager.getmPreferensesManager().getEmail());
                    Uri uri = Uri.parse(navData.get(2));
                    Picasso.with(this).load(uri).into(mAvatarView);

                }

    private void initPhoto() {
        insertProfileImage(mDataManager.getmPreferensesManager().loadUserPhoto());
    }

    private void saveUserField() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserFieldViews) {
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


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
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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

        mDataManager.getmPreferensesManager().saveUserPhoto(selectedImage);
    }

    public void openApplicationSettings(){
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMITION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Интент для совершения звонка по номеру телефона указанному в профиле
     * @param phoneNumber
     */
    private void profileCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (!phoneNumber.isEmpty()) {
                Uri uri = Uri.parse("tel:" + phoneNumber);
                Intent callProfileIntent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(callProfileIntent);
            } else {
                showSnackbar(getString(R.string.intent_call_err));
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE,
            }, ConstantManager.CALL_REQUEST_PERMISSION_CODE);
            //вызов настройки для выдачи разрешения-->
            Snackbar.make(mCoordinatorLayout, R.string.intent_request_permission, Snackbar.LENGTH_LONG)
                    .setAction(R.string.intent_ally, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    /**
     * Интент для отправки письма по указанному email
     * @param email
     */
    private void sendEmail(String email) {

        if (!email.isEmpty()) {
            Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            try {
                startActivity(Intent.createChooser(sendEmailIntent, getString(R.string.intent_chooser_email)));
            } catch (ActivityNotFoundException ex) {
                showSnackbar(getString(R.string.intent_email_client_err));

            }
        } else {
            showSnackbar(getString(R.string.intent_email_err));
        }
    }

    /**
     *Интент на открытие профиля VK
     * @param vkProfileUrl
     */
    private void openVkProfile(String vkProfileUrl){
        if (!vkProfileUrl.equals("") || !vkProfileUrl.equals("null")) {
            Intent vkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + vkProfileUrl));

            try {
                startActivity(Intent.createChooser(vkIntent,getString(R.string.intent_vk_open)));
            } catch (ActivityNotFoundException e) {
                showSnackbar(getString(R.string.intent_vk_app_err));
            }
        }
    }

    /**
     * Интент на открытие профиля git
     * @param gitProfileUrl
     */
    private void openGitProfile(String gitProfileUrl){
        if (!gitProfileUrl.equals("") || !gitProfileUrl.equals("null")) {
            Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + gitProfileUrl));

            try {
                startActivity(gitIntent);
            } catch (ActivityNotFoundException e) {
                showSnackbar(getString(R.string.intent_git_app_err));
            }
        }
    }

    private void uploadPhoto(Uri uri) {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            File file = new File(uri.getPath());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

            String descriptionString = "load photo";
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

            Call<ResponseBody> call = mDataManager.savePhoto(description, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    Log.v("Upload", "success");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());
                }
            });
        } else {
            showSnackbar("Сеть на данный момент не доступна");
        }
    }
}