package com.example.watchnext.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.watchnext.R;

public abstract class CameraUtilFragment extends Fragment {

    protected static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final int REQUEST_OPEN_GALLERY = 2;

    public void showCameraMenu(View view) {
        if (this.getContext() != null) {
            PopupMenu cameraPopupMenu = new PopupMenu(this.getContext(), view);
            MenuInflater inflater = cameraPopupMenu.getMenuInflater();
            inflater.inflate(R.menu.camera_menu, cameraPopupMenu.getMenu());
            cameraPopupMenu.setOnMenuItemClickListener(this::setOnMenuItemClickListener);
            cameraPopupMenu.show();
        }
    }

    private boolean setOnMenuItemClickListener(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.camera_menu_open_camera) {
            openCamera();
            return true;
        } else if (menuItem.getItemId() == R.id.camera_menu_open_gallery) {
            openGallery();
            return true;
        }
        return false;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),REQUEST_OPEN_GALLERY);
    }

}
