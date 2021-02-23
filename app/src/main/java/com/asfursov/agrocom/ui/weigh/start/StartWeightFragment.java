package com.asfursov.agrocom.ui.weigh.start;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asfursov.agrocom.R;

public class StartWeightFragment extends Fragment {

    private StartWeightViewModel mViewModel;

    public static StartWeightFragment newInstance() {
        return new StartWeightFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_weight_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StartWeightViewModel.class);
        // TODO: Use the ViewModel
    }

}