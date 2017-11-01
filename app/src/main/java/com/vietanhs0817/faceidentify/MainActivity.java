package com.vietanhs0817.faceidentify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.contract.TrainingStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://eastasia.api.cognitive.microsoft.com/face/v1.0", "7c0d30d090d04d40b7b2a19d9e7f6e67");
    private static final String personGroupId = "hollywoodstars";
    private ImageView imgTest;
    private Button btnIdentify;
    private Button btnCapture;
    private Bitmap bitmap;
    private Face[] faceDetecteds;
    private TextView txtView;

    private String personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
        imgTest = findViewById(R.id.imgTest);
//        imgTest.setImageBitmap(bitmap);
        btnIdentify = findViewById(R.id.btnIdentify);
        btnCapture = findViewById(R.id.btnCapture);
        txtView = findViewById(R.id.txt_message);
        txtView.setText("Capture image and Press Identify button");
        initData();
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
            }
        });

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personName != null) {
                    Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                    intent.putExtra("person", personName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No person", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("image") && intent.getParcelableExtra("image") != null) {

            Bitmap bitmapReceiver = (Bitmap) intent.getParcelableExtra("image");
//            Glide.with(this)
//                    .asBitmap()
//                    .load(bitmap)
//                    .into(imgTest);
            bitmap = bitmapReceiver;
            imgTest.setImageBitmap(bitmap);
            identify(bitmap);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
        if (bitmap != null) {
//        Matrix mat = new Matrix();
//        mat.postRotate(90);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);

            imgTest.setImageBitmap(bitmap);

            identify(bitmap);
        } else {
            Toast.makeText(this, "No bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    private void identify(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(outputStream.toByteArray());
            try {
                faceDetecteds = new DetectFace().execute(inputStream).get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (faceDetecteds == null || faceDetecteds.length == 0) {
                txtView.setText("No face detected");
                return;
            }
            final UUID[] faceIds = new UUID[faceDetecteds.length];
            for (int i = 0; i < faceIds.length; i++) {
                faceIds[i] = faceDetecteds[i].faceId;
            }
            new IdentifyFace(personGroupId).execute(faceIds);
        } else {
            Toast.makeText(MainActivity.this, "No bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    private class IdentifyFace extends AsyncTask<UUID, String, IdentifyResult[]> {

        String personGroupId;

        public IdentifyFace(String personGroupId) {
            this.personGroupId = personGroupId;
        }

        @Override
        protected IdentifyResult[] doInBackground(UUID... uuids) {
            try {
                publishProgress("Geting person groups status...");
                TrainingStatus trainingStatus = faceServiceClient.getPersonGroupTrainingStatus(this.personGroupId);
                if (trainingStatus.status != TrainingStatus.Status.Succeeded) {
                    publishProgress("Person group training status is : " + trainingStatus.status);
                    return null;
                }
                publishProgress("Identifying...");
                return faceServiceClient.identity(personGroupId, uuids, 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        private ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected void onPostExecute(IdentifyResult[] identifyResults) {
            mDialog.dismiss();
            if (identifyResults == null || identifyResults.length == 0) {
                txtView.setText("Sorry I don't know this person!");
                return;
            }
            for (IdentifyResult result : identifyResults) {
                if (!result.candidates.isEmpty()) {
                    new PersonDetection(personGroupId).execute(result.candidates.get(0).personId);
                } else {
                    txtView.setText("Sorry I don't know this person!");
                }

            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            mDialog.setMessage(values[0]);
        }
    }

    private class PersonDetection extends AsyncTask<UUID, String, Person> {

        private ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        private String personGroupId;

        public PersonDetection(String personGroupId) {
            this.personGroupId = personGroupId;
        }

        @Override
        protected Person doInBackground(UUID... uuids) {
            try {
                publishProgress("Geting person ...");
                return faceServiceClient.getPerson(personGroupId, uuids[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Person person) {
            mDialog.dismiss();
            ImageView img = findViewById(R.id.imgTest);
            img.setImageBitmap(drawRectangle(bitmap, faceDetecteds, person.name));
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mDialog.setMessage(values[0]);

        }

        private Bitmap drawRectangle(Bitmap bm, Face[] faceDeteceds, String name) {
            Bitmap bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(12);
            if (faceDeteceds != null) {
                StringBuilder sb = new StringBuilder();
                for (Face face : faceDeteceds) {
                    FaceRectangle faceRectangle = face.faceRectangle;
                    canvas.drawRect(faceRectangle.left, faceRectangle.top, faceRectangle.left + faceRectangle.width, faceRectangle.top + faceRectangle.height, paint);
                    sb.append("This is ").append(name).append("\n");
                }
                txtView.setText(sb.toString());
                personName = name;
            }
            return bitmap;
        }

    }

    private class DetectFace extends AsyncTask<InputStream, String, Face[]> {

        private ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected Face[] doInBackground(InputStream... params) {
            try {
                publishProgress("Detecting...");
                Face[] result = faceServiceClient.detect(
                        params[0],
                        true,         // returnFaceId
                        false,        // returnFaceLandmarks
                        null           // returnFaceAttributes: a string like "age, gender"
                );
                if (result == null) {
                    publishProgress("Detection Finished. Nothing detected");
                    return null;
                }
                publishProgress(
                        String.format("Detection Finished. %d face(s) detected",
                                result.length));
                return result;
            } catch (Exception e) {
                publishProgress("Detection failed");
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            mDialog.show();

        }

        @Override
        protected void onPostExecute(Face[] faces) {
            mDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mDialog.setMessage(values[0]);
        }
    }

}
