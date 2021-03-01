package com.asfursov.agrocom.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;

public abstract class ScanningFormFragment extends Fragment {
    protected View root;

    protected ImageButton scanBadgeButton;
    protected TextView information;
    protected ProgressBar progressBar;

    protected void initialize() {
        bindViews(getScanButtonView(), getInformationTextView(), getProgressBar());
        processParameters(getArguments());
        updateTitle();
        scanBadgeButton.setOnClickListener(provideScanButtonListener());

        dispatchBarcode();

        information.setText(getInitialText());
    }

    protected void processParameters(Bundle arguments) {
    }

    private void updateTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    protected abstract String getTitle();

    protected abstract int getProgressBar();

    @NotNull
    protected abstract String getInitialText();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(getFragmentId(), container, false);
        ButterKnife.bind(this, root);
        initialize();
        return root;
    }

    protected abstract int getInformationTextView();

    protected abstract int getScanButtonView();

    protected View.OnClickListener provideScanButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        };
    }

    protected void bindViews(int scanButtonId, int informationTextId, int progressBarId) {
        scanBadgeButton = root.findViewById(scanButtonId);
        information = root.findViewById(informationTextId);
        progressBar = root.findViewById(progressBarId);

    }

    protected void dispatchBarcode() {
        Bundle param = getArguments();
        String bc;
        if (param != null
                && param.containsKey(Constants.BARCODE)
                && ((bc = param.getString(Constants.BARCODE)) != null)) {
            processScannedBarcode(bc);
        }
    }

    protected void processScannedBarcode(String barcode) {
        startProgressIndicator();
        finishBarcodeProcessing(barcode);
    }

    protected void finishBarcodeProcessing(String barcode) {
        stopProgressIndicator();
    }

    protected void stopProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    protected void startProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void setErrorText(String text) {
        information.setText(text);
        information.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
    }

    protected void scanBarcode() {
        AppData.getInstance().setBarcodeScannerReturnAction(getScanningReturnAction());
        AppData.getInstance().setLastBarcode(null);
        ((MainActivity) getActivity()).getNavController().navigate(R.id.barcodeScanningFragment);
    }

    protected abstract int getScanningReturnAction();

    protected abstract int getFragmentId();
}
