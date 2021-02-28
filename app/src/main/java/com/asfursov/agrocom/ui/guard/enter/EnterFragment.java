package com.asfursov.agrocom.ui.guard.enter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterFragment extends Fragment {

    public static final String SCAN_DRIVER = "Отсканируйте код с браслета:";
    private EnterViewModel mViewModel;
    private OperationId operationId;
    @BindView(R.id.scanBadgeButtonEnter)
    ImageButton scanBadgeButton;
    @BindView(R.id.informationTextEnter)
    TextView information;
    @BindView(R.id.editTextPlate)
    EditText plateNumber;
    @BindView(R.id.progressBarEnter)
    ProgressBar progressBar;
    @BindView(R.id.buttonCommitEnter)
    Button buttonCommit;

    public static EnterFragment newInstance() {
        return new EnterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.enter_fragment, container, false);
        ButterKnife.bind(this, root);
        initialize();
        return root;
    }

    private void initialize() {
        scanBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        });
        Bundle param = getArguments();
        String bc;
        if (param != null
                && param.containsKey(Constants.BARCODE)
                && ((bc = param.getString(Constants.BARCODE)) != null)) {
            processScannedBarcode(bc);

            return;
        }

        information.setText(SCAN_DRIVER);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EnterViewModel.class);
        // TODO: Use the ViewModel
    }

    private void processScannedBarcode(String barcode) {
        information.setText(barcode);
        startProgressIndicator();
//        NetworkHelper.getInstance().getAPI().login(new LoginRequest(barcode, null)).enqueue(new Callback<UserData>() {
//            @Override
//            public void onResponse(Call<UserData> call, Response<UserData> response) {
//                stopProgressIndicator();
//                if(response.code()==403 && response.body()==null){
//                    AuthenticateUser(barcode,false);
//                    return;
//                };
//
//                if(response.code()==200 && response.body()!=null){
//                    AuthenticateUser(barcode,response.body().newPasswordRequired());
//                }
//                else
//                    setErrorText(WRONG_BARCODE);
//            }
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable t) {
//                stopProgressIndicator();
//                setErrorText(Constants.NETWORKING_ERROR+"\r+"+t.getMessage());
//                if(BuildConfig.DEBUG) Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
//            }
//        }) ;
    }

    private void stopProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void startProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setErrorText(String text) {
        information.setText(text);
        information.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
    }

    private void scanBarcode() {
        AppData.getInstance().setBarcodeScannerReturnAction(R.id.action_barcodeScanningFragment_to_enterFragment);
        AppData.getInstance().setLastBarcode(null);
        ((MainActivity) getActivity()).getNavController().navigate(R.id.action_enterFragment_to_barcodeScanningFragment);
    }


}