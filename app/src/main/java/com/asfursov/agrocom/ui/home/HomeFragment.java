package com.asfursov.agrocom.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.Role;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.state.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    @BindView(R.id.guardButton)
    Button guard;
    @BindView(R.id.weighButton)
    Button weigh;
    @BindView(R.id.unloadButton)
    Button unload;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        ButterKnife.bind(this,root);

        setVisibility();

        guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_guardFragment);

            }
        });

        weigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_weighFragment);

            }
        });
        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_unloadFragment);

            }
        });
        
        return root;
    }

    private void setVisibility() {
        UserData user = AppData.getInstance().getUser();
        if (user==null)
        {
            guard.setVisibility(View.GONE);
            weigh.setVisibility(View.GONE);
            unload.setVisibility(View.GONE);
        }
        else{
            if (user.hasRole(Role.GUARD))
                guard.setVisibility(View.VISIBLE);
            else
                guard.setVisibility(View.GONE);
            if (user.hasRole(Role.WEIGH))
                weigh.setVisibility(View.VISIBLE);
            else
                weigh.setVisibility(View.GONE);
            if (user.hasRole(Role.UNLOAD))
                unload.setVisibility(View.VISIBLE);
            else
                unload.setVisibility(View.GONE);
        }
    }
}