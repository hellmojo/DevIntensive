package com.softdesign.devintensive.utils;

/**
 * Created by JDK on 25.06.2016.
 */
public interface ConstantManager {
    String TAG_PREFIX="DEV ";
    String COLOR_MODE_KEY="COLOR_MODE_KEY";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_ABOUT_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE=99;
    int REQUEST_GALLERY_PICTURE=88;

    int PERMITION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    int CALL_REQUEST_PERMISSION_CODE = 103;
}