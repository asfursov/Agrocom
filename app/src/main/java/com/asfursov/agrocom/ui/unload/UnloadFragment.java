package com.asfursov.agrocom.ui.unload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asfursov.agrocom.R;

public class UnloadFragment extends Fragment {


    public static UnloadFragment newInstance() {
        return new UnloadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.unload_fragment, container, false);
    }

}