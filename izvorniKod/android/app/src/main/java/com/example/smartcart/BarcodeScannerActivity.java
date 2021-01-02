package com.example.smartcart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BarcodeScannerActivity extends AppCompatActivity {

    private static final int REQUEST_BARCODE = 1; // slicno kao u HomeScreenActivity ali 2. namjena
    private ExecutorService cameraExecutor;
    private BarcodeScanner barcodeScanner;
    private PreviewView previewView;
    private LinearLayout manualInputView;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        previewView = findViewById(R.id.camera_preview);
        manualInputView = findViewById(R.id.manual_input_div);
        handlePermissions();
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // ne treba provjeravati requestCode jer samo na jednom mjestu trazimo permission
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            // TODO: Makni PreviewView i osiguraj dohvaćanje inputa na pritisak gumba
        }
    }

    private void startScanning() {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_CODE_39,
                Barcode.FORMAT_CODE_93,
                Barcode.FORMAT_CODABAR,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_ITF,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E

        ).build();
        barcodeScanner = BarcodeScanning.getClient(options);
        ((ViewGroup) manualInputView.getParent()).removeView(manualInputView);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder()
                        .setTargetResolution(new Size(720, 960))
                        .build();
                ImageAnalysis imageAnalyzer = new ImageAnalysis.Builder().build();
                imageAnalyzer.setAnalyzer(cameraExecutor, new BarcodeAnalyzer(
                        barcodeScanner, this::returnResult));
                Camera camera = cameraProvider.bindToLifecycle(
                        this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture, imageAnalyzer
                );

                {   // Setting up auto focus
                    previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (previewView.getMeasuredWidth() > 0 && previewView.getMeasuredHeight() > 0) {
                                previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                previewView.setOnTouchListener((view, event) -> {
                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            return true;
                                        case MotionEvent.ACTION_UP:
                                            MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory(
                                                    previewView.getWidth(), previewView.getHeight()
                                            );
                                            MeteringPoint point = factory.createPoint(event.getX(), event.getY());
                                            Log.d("Barcode",point.toString());
                                            camera.getCameraControl().startFocusAndMetering(
                                                    new FocusMeteringAction.Builder(
                                                            point, FocusMeteringAction.FLAG_AF
                                                    ).disableAutoCancel().build()
                                            );
                                            view.performClick();
                                            return true;
                                        default:
                                            return false;

                                    }
                                });
                            }
                        }
                    });

                }
            } catch (ExecutionException | InterruptedException e) {
                Log.e("Barcode", e.toString(), e);
            }

        }, getMainExecutor());
    }

    @Override
    public Executor getMainExecutor() { // Not implemented in some versions of Android
        try {
            return super.getMainExecutor();
        } catch (NoSuchMethodError nsme) {
            return ContextCompat.getMainExecutor(this);
        }
    }

    private void handlePermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        } else {
            requestPermissions(new String[] { Manifest.permission.CAMERA},  REQUEST_BARCODE);
        }
    }

    private void returnResult(String barcode) {
        Toast.makeText(this, barcode, Toast.LENGTH_LONG).show();
        // Intent resultIntent = new Intent();
        // resultIntent.putExtra("barcode", barcode);
        // setResult(RESULT_OK, resultIntent);
        // finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        barcodeScanner.close();
    }

    // Crne magije i crne kutije
    // Više informacija: https://developers.google.com/ml-kit/vision/barcode-scanning/android
    private static class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
        private final BarcodeScanner barcodeScanner;
        private final Consumer<String> returnResult;
        public BarcodeAnalyzer(
                BarcodeScanner barcodeScanner, Consumer<String> returnResult
        ) {
            this.barcodeScanner = barcodeScanner;
            this.returnResult = returnResult;
        }


        @RequiresApi(Build.VERSION_CODES.KITKAT)
        @ExperimentalGetImage
        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                Log.d("Barcode", "analyzing");
                barcodeScanner.process(image)
                        .addOnSuccessListener(barcodes -> {
                            Log.d("Barcode", "# of barcodes = " + barcodes.size());
                            if (barcodes.size() > 0) {
                                returnResult.accept(barcodes.get(0).getDisplayValue());
                            }
                            imageProxy.close();
                        })
                        .addOnFailureListener(e -> Log.e("Barcode", e.toString(), e));
            }
        }
    }
}