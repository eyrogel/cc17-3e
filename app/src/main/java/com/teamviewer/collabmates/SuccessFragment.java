package com.teamviewer.collabmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SuccessFragment extends Fragment {

    public SuccessFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        ImageButton closeButton = view.findViewById(R.id.imageButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle closing the SuccessFragment and going back to WithdrawFragment
                closeSuccessFragment();
            }
        });

        return view;
    }

    private void closeSuccessFragment() {
        // Remove the SuccessFragment from the back stack
        requireFragmentManager().popBackStack();

        // Show the withdrawLayout in WithdrawFragment
        showWithdrawLayout();

        // Reset the selected image
        resetSelectedImage();
    }

    private void showWithdrawLayout() {
        // Get the parent fragment (WithdrawFragment)
        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof WithdrawFragment) {
            // Show the withdrawLayout in WithdrawFragment
            ((WithdrawFragment) parentFragment).showWithdrawLayout();
        }
    }

    private void resetSelectedImage() {
        // Get the parent fragment (WithdrawFragment)
        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof WithdrawFragment) {
            // Reset the selected image in WithdrawFragment
            ((WithdrawFragment) parentFragment).resetSelectedImage();
        }
    }
}
