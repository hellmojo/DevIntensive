package com.softdesign.devintensive.utils;

public interface ConstantManager {
    String TAG_PREFIX="DEV ";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String PARCELABLE_KEY = "PARCELABLE_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE=99;
    int REQUEST_GALLERY_PICTURE=88;
    /**
     * Коды доступа
     */
    int PERMITION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    int CALL_REQUEST_PERMISSION_CODE = 103;
    /**
     * Авторизация
     */
    String AUTH_TOKEN_KEY= "AUTH_TOKEN_KEY";
    String USER_ID_KEY= "USER_ID_KEY";
    /**
     * Значение данных получаемых по сети
     */
    //==========USER CODING INFO=========
    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECTS_VALUE = "USER_PROJECTS_VALUE";
//==========USER CONTENT INFO===============
    String CONTENT_PHONE_VALUES = "CONTENT_PHONE_VALUES";
    String CONTENT_EMAIL_VALUES = "CONTENT_EMAIL_VALUES";
    String CONTENT_GIT_VALUES = "CONTENT_GIT_VALUES";
    String CONTENT_VK_VALUES = "CONTENT_VK_VALUES";
    String CONTENT_BIO_VALUES = "CONTENT_BIO_VALUES";
//===============USER NAVIGATION BAR INFO==========
    String NAV_FIRST_NAME_VALUES = "NAV_FIRST_NAME_VALUES";
    String NAV_SECOND_NAME_VALUES = "NAV_SECOND_NAME_VALUES";
    String NAV_AVATAR_VALUES = "NAV_AVATAR_VALUES";

}
