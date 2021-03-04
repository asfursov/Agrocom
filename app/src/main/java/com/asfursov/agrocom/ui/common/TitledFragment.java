package com.asfursov.agrocom.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

public abstract class TitledFragment extends Fragment {
    protected View rootView;

    protected void initialize() {

        processParameters(getArguments());
        updateTitle();
    }


    protected void processParameters(Bundle arguments) {
    }

    protected void updateTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        if (getTitle() == null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        else
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }

    protected abstract String getTitle();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getFragmentId(), container, false);
        ButterKnife.bind(this, rootView);
        initialize();
        return rootView;
    }

    protected abstract int getFragmentId();
}
