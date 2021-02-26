package com.asfursov.agrocom.ui.login;

import androidx.constraintlayout.widget.Group;
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
import android.widget.TextView;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.network.APIHelper;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asfursov.agrocom.state.Constants.MESSAGE;

public class LoginFragment extends Fragment {

    public static final String USER_SUCCESSULLY_LOGIN = "Пользователь успешно авторизован";
    public static final String WRONG_BARCODE = "Неправильный штрихкод. Повторите попытку";
    public static final String ENTER_PASSWORD = "Введите пароль:";
    public static final String SCAN_BADGE = "Просканируйте бейдж пользователя";
    private LoginViewModel mViewModel;

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

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this,root);
        Initialize();
        return root;
    }

    private void Initialize() {
        Bundle param = getArguments();
        if(param!=null && param.getString(Constants.BARCODE)!=null) {
            processScannedBarcode();
            return;
        }

        infotext.setText(SCAN_BADGE);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBarcode();
            }
        });

    }

    private void processScannedBarcode() {
        UserData user = APIHelper.findUserByBarcode(AppData.getInstance().getLastBarcode());
        if (user!=null)
            AuthenticateUser(user);
        else
        {
            infotext.setText(WRONG_BARCODE);
        }
    }

    private void AuthenticateUser(UserData user) {
        scanButton.setVisibility(View.GONE);

        infotext.setText(ENTER_PASSWORD);
        passwordGroup.setVisibility(View.VISIBLE);

        if(user.newPasswordRequired())
            groupConfirmPassword.setVisibility(View.VISIBLE);
        else
            groupConfirmPassword.setVisibility(View.GONE);

        confirmButton.setEnabled(false);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword(user);
            }
        });
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

    private void checkPassword(UserData user) {
        UserData newUser = APIHelper.AuthorizeUser(user.getId(),password.getText());
        if  (newUser.authorized()) {
            AppData.getInstance().setUser(user);
            Bundle params = new Bundle();
            params.putString(MESSAGE, USER_SUCCESSULLY_LOGIN);
            ((MainActivity) getActivity()).getNavController().navigate(R.id.resultFragment, params);
        }

    }

    private void ScanBarcode() {
        AppData.getInstance().setBarcodeScannerReturnAction(R.id.action_barcodeScanningFragment_to_nav_login);
        AppData.getInstance().setLastBarcode(null);
        ((MainActivity)getActivity()).getNavController().navigate(R.id.action_nav_login_to_barcodeScanningFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

}