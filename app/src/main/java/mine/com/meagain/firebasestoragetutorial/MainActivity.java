package mine.com.meagain.firebasestoragetutorial;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        imageView = (ImageView) findViewById(R.id.imageView);

    }

    public void uploadFile() {

        Uri file = Uri.fromFile(new File("path/to/images/ic_launcher_round.png"));
        StorageReference riversRef = mStorageRef.child("images/ic_launcher_round.png");
        // Get the data from an ImageView as bytes

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(MainActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void downloadFile() {

        StorageReference riversRef = mStorageRef.child("images/ic_launcher_round.png");

// Load the image using Glide
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(riversRef)
                .into(imageView);
//        File localFile = null;
//        try {
//            localFile = File.createTempFile("images", "png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        StorageReference riversRef = mStorageRef.child("images/ic_launcher_round.png");
//        riversRef.getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // Successfully downloaded data to local file
//                        // ...
//
//                        Toast.makeText(MainActivity.this, "Downloaded!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle failed download
//                // ...
//            }
//        });

    }

    public void uploadButtonClicked(View view) {
        uploadFile();
    }

    public void downloadButtonClicked(View view) {
        downloadFile();
    }
}
