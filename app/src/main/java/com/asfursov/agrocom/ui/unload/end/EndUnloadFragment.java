package com.asfursov.agrocom.ui.unload.end;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asfursov.agrocom.R;

public class EndUnloadFragment extends Fragment {

    private EndUnloadViewModel mViewModel;

    public static EndUnloadFragment newInstance() {
        return new EndUnloadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.end_unload_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EndUnloadViewModel.class);
        // TODO: Use the ViewModel
    }

}