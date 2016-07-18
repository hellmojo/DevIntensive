package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSignIn;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;
    private CoordinatorLayout mCoordinatorLayout;
    private DataManager mDataManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mDataManager = DataManager.getInstanse();


        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mLogin = (EditText)findViewById(R.id.auth_login);
        mPassword = (EditText)findViewById(R.id.auth_password);
        mSignIn = (Button)findViewById(R.id.auth_button);
        mRememberPassword = (TextView)findViewById(R.id.auth_forgot_pass);

        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.auth_button:
                signIn();
                break;
            case R.id.auth_forgot_pass:
                rememberPassword();
                break;
        }

    }


    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message,Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword(){
        Intent rememberIntent  = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSucces(UserModelRes userModel){
        mDataManager.getmPreferensesManager().saveAuthToken(userModel.getData().getToken());
        Log.d("TOKEN", userModel.getData().getToken());
        mDataManager.getmPreferensesManager().saveUserId(userModel.getData().getUser().getId());
             saveUserValues(userModel);
       saveContentValues(userModel);
        saveNavValues(userModel);
        savePhoto(userModel);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }
    private void signIn(){
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(),
                    mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSucces(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль");
                    } else {
                        showSnackbar("Все пропало!");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 11.07.2016 обработать ошибки
                }
            });
        }else {
            showSnackbar("Сеть на данный момент не доступна");
        }
    }

    private void saveUserValues(UserModelRes userModel){
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };
        mDataManager.getmPreferensesManager().saveUserProfileValue(userValues);
    }

    private void saveContentValues(UserModelRes userModel){
        String[] contentValues = {
                userModel.getData().getUser().getContacts().getPhone(),
                userModel.getData().getUser().getContacts().getEmail(),
                userModel.getData().getUser().getRepositories().getRepo().get(0).getGit(),
                userModel.getData().getUser().getContacts().getVk(),
                userModel.getData().getUser().getPublicInfo().getBio()

        };
        mDataManager.getmPreferensesManager().saveContentValue(contentValues);
    }
    private void saveNavValues(UserModelRes userModel){
        String[] navValues = {
                userModel.getData().getUser().getFirstName(),
                userModel.getData().getUser().getSecondName(),
                userModel.getData().getUser().getPublicInfo().getAvatar()
        };
        mDataManager.getmPreferensesManager().saveNavValue(navValues);
    }

    private void savePhoto(UserModelRes userModel){
        mDataManager.getmPreferensesManager().saveUserPhoto(Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto()));
    }

}
