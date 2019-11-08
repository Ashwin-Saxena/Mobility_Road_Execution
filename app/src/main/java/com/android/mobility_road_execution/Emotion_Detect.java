package com.android.mobility_road_execution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class Emotion_Detect extends AppCompatActivity {
ImageView imageView;
TextView emo;
    public Bitmap selectedImage;
    public int RESULT_LOAD_IMG=999;
Button takepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion__detect);

        emo=findViewById(R.id.textView2);
        imageView=findViewById(R.id.imageView2);
        takepic=findViewById(R.id.button);
        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opengllery();
            }
        });

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("model")
                .setAssetFilePath("manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);

    }
    private void opengllery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Emotion_Detect.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Emotion_Detect.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
        try {
            imagebitmap(selectedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (resultCode == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(Emotion_Detect.this, "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        }else {
//            Toast.makeText(Emotion_Detect.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
//        try {
//            imagebitmap(selectedImage);
//        } catch (FirebaseMLException e) {
//            e.printStackTrace();
//        }

    }

    private void imagebitmap(Bitmap bitmap) throws FirebaseMLException {
        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("model")    // Skip to not use a local model
                        .build();
        FirebaseVisionImageLabeler labeler =
                FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
        imageView.setImageBitmap(bitmap);
        labeler.processImage(FirebaseVisionImage.fromBitmap(bitmap))
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        for (FirebaseVisionImageLabel label: labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            String tt = emo.getText().toString();
                            emo.setText("\nThe Person is : "+text);
                        }

                        Toast.makeText(Emotion_Detect.this,"Succ",Toast.LENGTH_SHORT).show();


                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Toast.makeText(Emotion_Detect.this,"Error"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        // ...
                    }
                });

    }
}
