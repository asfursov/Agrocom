package com.asfursov.agrocom.ui.operation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.EnterLeaveRequest;
import com.asfursov.agrocom.model.OperationAllowedRequest;
import com.asfursov.agrocom.model.OperationAllowedResponse;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.model.VehicleData;
import com.asfursov.agrocom.network.NetworkHelper;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asfursov.agrocom.state.Constants.ERROR;
import static com.asfursov.agrocom.state.Constants.MESSAGE;

public class OperationFragment extends com.asfursov.agrocom.ui.common.ScanningFormFragment {

    public static final String SCAN_DRIVER = "Отсканируйте код с браслета:";
    public static final String NOT_ALLOWED = "НЕ РАЗРЕШЕНО\n";
    @BindView(R.id.buttonCommitOperation)
    Button buttonCommit;

    private OperationId operationId;
    @BindView(R.id.plateConfirmGroup)
    Group plateGroup;

    @BindView(R.id.editTextPlate)
    EditText plateNumber;
    private VehicleData vehicle;

    @Override
    protected void initialize() {
        super.initialize();

        plateGroup.setVisibility(View.GONE);

        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitOperation();
            }
        });
    }

    private void commitOperation() {
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().operate(
                new EnterLeaveRequest(vehicle,
                        AppData.getInstance().getUser(), plateNumber.getText().toString(),
                        operationId)
        ).enqueue(new Callback<OperationAllowedResponse>() {
            @Override
            public void onResponse(Call<OperationAllowedResponse> call, Response<OperationAllowedResponse> response) {
                stopProgressIndicator();

                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isAllowed())
                        processConfirmed(response.body());
                    else
                        processRejected(response.body().getMessage());
                } else
                    processRejected(response.message());

            }

            @Override
            public void onFailure(Call<OperationAllowedResponse> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\r+" + t.getMessage());


            }
        });

    }

    public static OperationFragment newInstance() {
        return new OperationFragment();
    }

    @Override
    protected void processParameters(Bundle arguments) {
        super.processParameters(arguments);
        operationId = AppData.getInstance().getOperationId();
    }

    @Override
    protected String getTitle() {
        return operationId.getName().toUpperCase();
    }


    @Override
    protected int getInformationTextView() {
        return R.id.informationTextOperation;
    }

    @NotNull
    @Override
    protected String getInitialText() {
        return SCAN_DRIVER;
    }

    @Override
    protected int getScanButtonView() {
        return R.id.scanBadgeButtonOperation;
    }

    @Override
    protected int getScanningReturnAction() {
        return R.id.action_barcodeScanningFragment_to_enterFragment;
    }

    @Override
    protected int getProgressBar() {
        return R.id.progressBarOperation;
    }


    @Override
    protected int getFragmentId() {
        return R.layout.operation_fragment;
    }

    @Override
    protected void processScannedBarcode(String barcode) {
        information.setText("Проверяем штрихкод");
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().isAllowed(
                new OperationAllowedRequest(
                        barcode,
                        AppData.getInstance().getUser().getId().toString(),
                        operationId)
        ).enqueue(new Callback<OperationAllowedResponse>() {
            @Override
            public void onResponse(Call<OperationAllowedResponse> call, Response<OperationAllowedResponse> response) {
                stopProgressIndicator();

                if (response.code() == 200 && response.body() != null) {
                    processAllowance(barcode, response.body());
                } else
                    setErrorText(WRONG_BARCODE + "\n" + decodeResponseMessage(response));

            }

            @Override
            public void onFailure(Call<OperationAllowedResponse> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\n+" + t.getMessage());


            }

        });
    }

    private String decodeResponseMessage(Response<OperationAllowedResponse> response) {
        if (response.body() != null)
            return response.body().getMessage();
        if (response.errorBody() != null) {
            try {
                JsonObject obj = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                return obj.get("message").getAsString();
            } catch (Throwable t) {
                try {
                    return response.errorBody().string();
                } catch (IOException e) {
                }
            }
        }
        return response.message();
    }

    private void processAllowance(String barcode, OperationAllowedResponse body) {
        String docdata = String.format(
                "Документ прибытия №%s.\n" +
                        "%s\n" +
                        "%s\n",

                body.getVehicle().getNumber(),
                body.getVehicle().getDriver(),
                body.getVehicle().getPhone());
        if (!body.isAllowed()) {
            setErrorText(docdata + NOT_ALLOWED + body.getMessage());
            plateGroup.setVisibility(View.GONE);
            scanBadgeButton.setVisibility(View.VISIBLE);
        } else {
            information.setText(docdata);
            information.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            scanBadgeButton.setVisibility(View.GONE);
            plateGroup.setVisibility(View.VISIBLE);
            vehicle = body.getVehicle();
        }
    }


    private void processRejected(String message) {
        setErrorText(message);
        buttonCommit.setText("OK");
        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle params = new Bundle();
                params.putString(ERROR, "");
                params.putString(MESSAGE, String.format(
                        "Операция '%s' завершена c ошибкой\n%s", operationId.getName(), message)
                );
                ((MainActivity) getActivity()).getNavController().navigate(R.id.resultFragment, params);
            }
        });
    }

    private void processConfirmed(OperationAllowedResponse body) {

        Bundle params = new Bundle();
        params.putString(MESSAGE, String.format(
                "Операция '%s' успешно завершена", operationId.getName())
        );
        ((MainActivity) getActivity()).getNavController().navigate(R.id.resultFragment, params);
    }
}