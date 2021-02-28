package com.asfursov.agrocom.ui.login;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asfursov.agrocom.BuildConfig;
import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.LoginRequest;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.network.NetworkHelper;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asfursov.agrocom.state.Constants.MESSAGE;

public class LoginFragment extends Fragment {

    public static final String USER_SUCCESSFULLY_LOGGED_IN = "Пользователь успешно авторизован";
    public static final String WRONG_BARCODE = "Неправильный штрихкод. Повторите попытку";
    public static final String ENTER_PASSWORD = "Введите пароль:";
    public static final String SCAN_BADGE = "Просканируйте бейдж пользователя";
    public static final String AUTH_ERROR = "Ошибка авторизации.\rПовторите ввод пароля:";
    private int defColor;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    
    @BindView(R.id.scanBadgeButton)
    ImageButton scanButton;
    @BindView(R.id.informationText)
    TextView infotext;
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
    @BindView(R.id.progressBarLogin)
    ProgressBar progressBar;

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this,root);
        Initialize();
        return root;
    }

    private void Initialize() {

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBarcode();
            }
        });

        Bundle param = getArguments();
        String bc;
        if(param!=null
            && param.containsKey(Constants.BARCODE)
                && ((bc=param.getString(Constants.BARCODE))!=null)) {
            processScannedBarcode(bc);
            return;
        }

        infotext.setText(SCAN_BADGE);

    }

    private void processScannedBarcode(String barcode) {
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().login(new LoginRequest(barcode, null)).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                stopProgressIndicator();
                if(response.code()==403 && response.body()==null){
                    AuthenticateUser(barcode,false);
                    return;
                };

                if(response.code()==200 && response.body()!=null){
                    AuthenticateUser(barcode,response.body().newPasswordRequired());
                }
                else
                    setErrorText(WRONG_BARCODE);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR+"\r+"+t.getMessage());
                if(BuildConfig.DEBUG) Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) ;
    }

    private void stopProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void startProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void setErrorText(String text) {
        infotext.setText(text);
        infotext.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
    }

    private void AuthenticateUser(String barcode,  Boolean newPasswordRequired) {
        scanButton.setVisibility(View.GONE);

        infotext.setText(ENTER_PASSWORD);
        infotext.setTextColor(ContextCompat.getColor(getContext(),R.color.green));

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

    private void ScanBarcode() {
        AppData.getInstance().setBarcodeScannerReturnAction(R.id.action_barcodeScanningFragment_to_nav_login);
        AppData.getInstance().setLastBarcode(null);
        ((MainActivity)getActivity()).getNavController().navigate(R.id.action_nav_login_to_barcodeScanningFragment);
    }

}