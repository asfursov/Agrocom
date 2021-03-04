package com.asfursov.agrocom.ui.logout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.ui.common.TitledFragment;

import butterknife.BindView;

public class LogoutFragment extends TitledFragment {

    @BindView(R.id.logoutButton)
    Button logoutButton;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    protected void initialize() {
        super.initialize();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.getInstance().setUser(null);
                ((MainActivity) getActivity()).UpdateMenu();
            }
        });
    }

    @Override
    protected String getTitle() {
        return "ВЫХОД";
    }

    @Override
    protected int getFragmentId() {
        return R.layout.logout_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}