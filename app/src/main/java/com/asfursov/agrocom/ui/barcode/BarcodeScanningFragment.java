package com.asfursov.agrocom.ui.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarcodeScanningFragment extends Fragment  {


    @BindView(R.id.surface_view)
    protected SurfaceView surfaceView;
    @BindView(R.id.barcode_text)
    protected TextView barcodeText;
    @BindView(R.id.barcodeAcceptedButton)
    protected ImageButton barcodeAcceptedButton;
    @BindView(R.id.startAgainButton)
    protected ImageButton startAgainButton;
    @BindView(R.id.flashToggleButton)
    protected ImageButton flashToggleButton;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private boolean flashState = false;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    static boolean isShot = false;

    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen;

    private String barcodeData;

    public static Camera getCamera(CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    return camera;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return null;
    }

    private void InitializeOnClickListeners() {
        barcodeAcceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PutBarcodeToClipboard();
                onBarcodeScanned();

            }
        });
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RunScanning();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        flashToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashState = !flashState;
                try {
                    setFlash(flashState);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(flashState) flashToggleButton.setImageResource(R.drawable.ic_flash_off_black_24);
                else flashToggleButton.setImageResource(R.drawable.ic_flash_on_black_24);
            }
        });
        toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    private void onBarcodeScanned() {
        AppData.getInstance().setLastBarcode(barcodeData);
        try {
            int barcodeScannerReturnAction = AppData.getInstance().getBarcodeScannerReturnAction();

            AppData.getInstance().setBarcodeScannerReturnAction(0);

            Bundle param = new Bundle();
            param.putString(Constants.BARCODE,barcodeData);
            ((MainActivity) getActivity()).getNavController().navigate(
                    barcodeScannerReturnAction,param
            );
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage()+"\r"+e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void PutBarcodeToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) (getActivity().getSystemService(getContext().CLIPBOARD_SERVICE));
        ClipData clip = ClipData.newPlainText("code", barcodeData);
        clipboard.setPrimaryClip(clip);
    }


    public String getScannedBarcode() {
        return barcodeData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_barcode_scanning, container, false);
        ButterKnife.bind(this, rootView);
        isShot = false;
        InitializeOnClickListeners();
        return rootView;
    }

    @SuppressLint("MissingPermission")
    private void RunScanning() throws IOException {
        cameraSource.start(surfaceView.getHolder());
        setFlash(flashState);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1024, 512)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        RunScanning();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && !isShot) {

                    isShot = true;

                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 500);
                                cameraSource.stop();
                                onBarcodeScanned();
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 500);
                                cameraSource.stop();
                                onBarcodeScanned();

                            }
                        }
                    });

                }
            }
        });
    }


    public void setFlash (boolean active) throws IOException {
        getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Camera _cam = getCamera (cameraSource);
        if (_cam != null) {
            Camera.Parameters _pareMeters = _cam.getParameters();
            _pareMeters.setFlashMode(active?Camera.Parameters.FLASH_MODE_TORCH:Camera.Parameters.FLASH_MODE_OFF);
            _cam.setParameters(_pareMeters);
            _cam.startPreview();
        }
    }
}


