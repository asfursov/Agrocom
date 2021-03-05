package com.asfursov.agrocom.ui.home;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.Role;
import com.asfursov.agrocom.model.UserData;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.ui.common.TitledFragment;

import butterknife.BindView;

public class HomeFragment extends TitledFragment {

    private static final String TITLE = "ГЛАВНАЯ";
    private HomeViewModel homeViewModel;

    @BindView(R.id.guardButton)
    Button guard;
    @BindView(R.id.weighButton)
    Button weigh;
    @BindView(R.id.unloadButton)
    Button unload;
    @BindView(R.id.greetingTextView)
    TextView greetingsText;


    @Override
    protected String getTitle() {
        return TITLE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setVisibility();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectByRole(Role.GUARD);

            }
        });

        weigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectByRole(Role.WEIGH);
            }
        });

        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectByRole(Role.UNLOAD);

            }
        });
    }

    private void redirectByRole(Role role) {
        AppData.getInstance().setRole(role);
        ((MainActivity) getActivity()).getNavController().navigate(getNavigationByRole(role));
    }

    private int getNavigationByRole(Role role) {
        switch (role) {
            case GUARD:
                return R.id.action_nav_home_to_guardFragment;
            case WEIGH:
                return R.id.action_nav_home_to_weighFragment;
            case UNLOAD:
                return R.id.action_nav_home_to_unloadFragment;
        }
        return R.id.nav_home;
    }


    @Override
    protected int getFragmentId() {
        return R.layout.home_fragment;
    }

    private void setVisibility() {
        UserData user = AppData.getInstance().getUser();
        if (user == null) {
            guard.setVisibility(View.GONE);
            weigh.setVisibility(View.GONE);
            unload.setVisibility(View.GONE);
        }
        else {

            int roles = 0;
            Role role = null;
            if (user.hasRole(Role.GUARD)) {
                guard.setVisibility(View.VISIBLE);
                role = Role.GUARD;
                roles++;
            } else
                guard.setVisibility(View.GONE);

            if (user.hasRole(Role.WEIGH)) {
                weigh.setVisibility(View.VISIBLE);
                role = Role.WEIGH;
                roles++;
            } else
                weigh.setVisibility(View.GONE);
            if (user.hasRole(Role.UNLOAD)) {
                unload.setVisibility(View.VISIBLE);
                role = Role.UNLOAD;
                roles++;

            } else
                unload.setVisibility(View.GONE);
            if (roles == 0) {
                greetingsText.setVisibility(View.VISIBLE);
                greetingsText.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else
                greetingsText.setVisibility(View.GONE);
            if (roles == 1) redirectByRole(role);
        }
    }
}