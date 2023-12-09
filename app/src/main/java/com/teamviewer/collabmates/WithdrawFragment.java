package com.teamviewer.collabmates;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WithdrawFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView qrImage;
    private Button uploadQrButton;
    private Button submitQrButton;
    private ConstraintLayout withdrawLayout;
    private RelativeLayout successLayout;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);

        qrImage = view.findViewById(R.id.qrImage);
        uploadQrButton = view.findViewById(R.id.uploadQrButton);
        submitQrButton = view.findViewById(R.id.submitQrButton);
        withdrawLayout = view.findViewById(R.id.withdrawLayout);
        successLayout = view.findViewById(R.id.successLayout);

        uploadQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        submitQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    // Proceed with the submission logic
                    // You can access the selectedImageUri to get the uploaded image
                    // For example, you can use selectedImageUri.getPath() to get the file path

                    // Hide the withdrawLayout
                    withdrawLayout.setVisibility(View.GONE);

                    // Show the success layout
                    showSuccessFragment();
                } else {
                    // Show an error message if no image has been uploaded
                    Toast.makeText(getActivity(), "Please Upload QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add the rest of your logic here...

        return view;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Comment out or remove the following line to avoid displaying the image immediately
            // qrImage.setImageURI(selectedImageUri);
        }
    }

    public void showWithdrawLayout() {
        // Show the withdrawLayout
        withdrawLayout.setVisibility(View.VISIBLE);
    }

    public void resetSelectedImage() {
        // Reset the selected image
        selectedImageUri = null;
        // Optionally, clear the displayed image if needed
        // qrImage.setImageURI(null);
    }

    // ...

    private void showSuccessFragment() {
        // Create an instance of SuccessFragment
        SuccessFragment successFragment = new SuccessFragment();

        // Replace the current fragment with SuccessFragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.successLayout, successFragment);
        transaction.addToBackStack(null); // Optional: Add to back stack if needed
        transaction.commit();
    }
}