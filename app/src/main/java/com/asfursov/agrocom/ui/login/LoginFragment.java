package com.asfursov.agrocom.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }
    
    @BindView(R.id.scanBadgeButton)
    ImageButton scanButton;
    @BindView(R.id.informationText)
    TextView infotext;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this,root);
        Initialize();
        return root;
    }

    private void Initialize() {
        switch(AppData.GetInstance().getBarcodeScannerReturnAction()) {
            case R.id.action_barcodeScanningFragment_to_nav_login:
            {
                processScannedBarcode();
                break;
            }
            default:
            infotext.setText("Просканируйте бейдж пользователя");
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBarcode();
            }
        });

    }

    private void processScannedBarcode() {
        infotext.setText(AppData.GetInstance().getLastBarcode());
    }

    private void ScanBarcode() {
        AppData.GetInstance().setBarcodeScannerReturnAction(R.id.action_barcodeScanningFragment_to_nav_login);
        AppData.GetInstance().setLastBarcode(null);
        ((MainActivity)getActivity()).getNavController().navigate(R.id.action_nav_login_to_barcodeScanningFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

}