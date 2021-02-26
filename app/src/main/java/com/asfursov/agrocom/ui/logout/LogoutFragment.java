package com.asfursov.agrocom.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogoutFragment extends Fragment {

    private LogoutViewModel mViewModel;
    @BindView(R.id.logoutButton)
    Button logoutButton;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.logout_fragment, container, false);
        ButterKnife.bind(this,root);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppData.GetInstance().setUser(null);
                ((MainActivity)getActivity()).UpdateMenu();
            }
        });
        //Initialize();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        // TODO: Use the ViewModel
    }

}