package com.example.dharkael.tweeter.ui.login;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.dharkael.tweeter.R;
import com.example.dharkael.tweeter.TweeterApp;
import com.example.dharkael.tweeter.api.LoginResponse;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.databinding.ActivityLoginBinding;
import com.example.dharkael.tweeter.ui.login.LoginViewModel.Resource;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class LoginActivity extends LifecycleActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ActivityLoginBinding activityBinding;
    LoginViewModel viewModel;
    LiveData<Resource<LoginResponse>> loginData;

    public static Intent intentForStart(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TweeterApp) getApplication()).appComponent().inject(this);
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        bindViewModel();
    }

    void bindViewModel() {
        viewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(LoginViewModel.class);
        activityBinding
                .editTextLoginUsername
                .addTextChangedListener(new TextChangedWatcher(viewModel::setUsername));
        activityBinding
                .editTextLoginPassword
                .addTextChangedListener(new TextChangedWatcher(viewModel::setPassword));

        activityBinding
                .buttonLoginSubmit
                .setOnClickListener(v -> {
                    loginData = viewModel.login();
                    loginData.observe(this, this::processLoginResource);
                });

        viewModel
                .isValidLoginData()
                .observe(this, valid -> activityBinding.buttonLoginSubmit.setEnabled(valid == Boolean.TRUE));

    }

    void showOverlay(boolean show) {
        if (show) {
            activityBinding.viewLoginOverlay.setVisibility(View.VISIBLE);
            activityBinding.viewLoginOverlay.requestFocus();
            //Dismiss Keyboard
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    activityBinding.editTextLoginPassword.getWindowToken(), 0);
        } else {
            activityBinding.viewLoginOverlay.setVisibility(View.GONE);
        }
    }


    void processLoginResource(@Nonnull Resource<LoginResponse> resource) {
        Log.i("LoginActivity", "processLoginResource" + resource.status);
        switch (resource.status) {
            case LOADING: {
                // Block/Disable UI
                showOverlay(true);
                return;
            }

            case SUCCESS: {
                final LoginResponse loginResponse = resource.data;
                if (loginResponse != null && loginResponse.isSuccessful()) {
                    Log.i("LoginActivity", "processLoginResource: " + loginResponse.userId);
                    final AuthenticatedUserId authenticatedUserId =
                            new AuthenticatedUserId(loginResponse.userId, new Date().getTime());
                    viewModel.setAuthenticatedUserId(authenticatedUserId);
                    Snackbar.make(activityBinding.buttonLoginSubmit, "Finishing with userID "+ authenticatedUserId.userId,Snackbar.LENGTH_LONG).show();
                    finish();
                    break;
                }
                //display error message/dialog
                if (loginResponse != null && loginResponse.error != null)
                    Snackbar.make(activityBinding.buttonLoginSubmit, "Error: " + loginResponse.error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                break;
            }
            case ERROR: {
                //display error message/dialog
                Snackbar.make(activityBinding.buttonLoginSubmit, "Error: " + resource.message, Snackbar.LENGTH_LONG).show();
                break;
            }

        }
        showOverlay(false);
        if (loginData != null) {
            loginData.removeObserver(this::processLoginResource);
            loginData = null;
        }
    }

    private static class TextChangedWatcher implements android.text.TextWatcher {
        private Consumer<CharSequence> consumer;

        TextChangedWatcher(Consumer<CharSequence> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                consumer.accept(s);
            } catch (Exception e) {
                Log.e("TextChangedWatcher", e.getMessage(), e);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}
