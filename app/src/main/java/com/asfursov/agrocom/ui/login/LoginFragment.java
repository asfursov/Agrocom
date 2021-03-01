package com.asfursov.agrocom.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.network.NetworkHelper;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asfursov.agrocom.state.Constants.MESSAGE;

public class LoginFragment extends com.asfursov.agrocom.ui.common.ScanningFormFragment {

    public static final String USER_SUCCESSFULLY_LOGGED_IN = "Пользователь успешно авторизован";
    public static final String WRONG_BARCODE = "Неправильный штрихкод. Повторите попытку";
    public static final String ENTER_PASSWORD = "Введите пароль:";
    public static final String SCAN_BADGE = "Просканируйте бейдж пользователя";
    public static final String AUTH_ERROR = "Ошибка авторизации.\rПовторите ввод пароля:";
    public static final String REQUEST_USER_DATA = "Запрашиваем данные пользователя...";

    public static final String TITLE = "Авторизация";

    @Override
    protected String getTitle() {
        return TITLE;
    }

    private int defColor;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @BindView(R.id.passwordGroup)
    Group passwordGroup;
    @BindView(R.id.groupConfirmPassword)
    Group groupConfirmPassword;
    @BindView(R.id.editTextTextPassword)
    EditText password;
    @BindView(R.id.editTextTextPassword2)
    EditText passwordConfirmation;
    @BindView(R.id.buttonCommitPassword)
    Button confirmButton;

    @NotNull
    @Override
    protected String getInitialText() {
        return SCAN_BADGE;
    }

    @Override
    protected int getInformationTextView() {
        return R.id.informationText;
    }

    @Override
    protected int getScanButtonView() {
        return R.id.scanBadgeButton;
    }


    @Override
    protected int getScanningReturnAction() {
        return R.id.action_barcodeScanningFragment_to_nav_login;
    }

    @Override
    protected void processScannedBarcode(String barcode) {
        information.setText(REQUEST_USER_DATA);
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().login(new LoginRequest(barcode, null)).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                stopProgressIndicator();
                if (response.code() == 403 && response.body() == null) {
                    authenticateUser(barcode, false);
                    return;
                }

                if(response.code()==200 && response.body()!=null){
                    authenticateUser(barcode, response.body().newPasswordRequired());
                } else
                    setErrorText(WRONG_BARCODE + "\r" + response.message());
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\r+" + t.getMessage());
                //if(BuildConfig.DEBUG) Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected int getProgressBar() {
        return R.id.progressBarLogin;
    }

    private void authenticateUser(String barcode, Boolean newPasswordRequired) {
        scanBadgeButton.setVisibility(View.GONE);

        information.setText(ENTER_PASSWORD);
        information.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword(barcode);
            }
        });

        passwordGroup.setVisibility(View.VISIBLE);

        if(newPasswordRequired){
            groupConfirmPassword.setVisibility(View.VISIBLE);
            confirmButton.setEnabled(false);

            TextWatcher listener = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(password.getText().toString().trim().equals(passwordConfirmation.getText().toString().trim())) confirmButton.setEnabled(true);
                }
            };
            password.addTextChangedListener(listener);
            passwordConfirmation.addTextChangedListener(listener);
        }
        else
            groupConfirmPassword.setVisibility(View.GONE);

    }

    private void checkPassword(String barcode) {
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().login(new LoginRequest(barcode,password.getText().toString())).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UserData newUser =response.body();
                if(response.code()==200 && newUser!=null){
                    stopProgressIndicator();
                    acceptNewUser(newUser);
                }
                else
                    setErrorText(AUTH_ERROR);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR);
            }
        }) ;

    }

    private void acceptNewUser(UserData user) {
        MainActivity main = ((MainActivity) getActivity());
        AppData.getInstance().setUser(user);
        main.UpdateUser();

        Bundle params = new Bundle();
        params.putString(MESSAGE, USER_SUCCESSFULLY_LOGGED_IN);
        main.getNavController().navigate(R.id.resultFragment, params);
    }


    protected int getFragmentId() {
        return R.layout.login_fragment;
    }
}