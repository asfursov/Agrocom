package com.asfursov.agrocom.ui.common;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import org.jetbrains.annotations.NotNull;

public abstract class ScanningFormFragment extends TitledFragment {

    public static final String SCANNER = "СКАННЕР";
    protected ImageButton scanBadgeButton;
    protected TextView information;
    protected ProgressBar progressBar;

    @Override
    protected String getTitle() {
        return SCANNER;
    }

    @Override
    protected void initialize() {
        super.initialize();
        bindViews(getScanButtonView(), getInformationTextView(), getProgressBar());
        processParameters(getArguments());
        updateTitle();
        scanBadgeButton.setOnClickListener(provideScanButtonListener());

        dispatchBarcode();

        information.setText(getInitialText());
    }

    protected abstract int getProgressBar();

    @NotNull
    protected abstract String getInitialText();

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
        scanBadgeButton = rootView.findViewById(scanButtonId);
        information = rootView.findViewById(informationTextId);
        progressBar = rootView.findViewById(progressBarId);

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

}
