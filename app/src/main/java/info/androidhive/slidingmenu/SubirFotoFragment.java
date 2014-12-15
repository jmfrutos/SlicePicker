package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SubirFotoFragment extends Fragment{

    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    private String mCurrentPhotoPath2 = null;
    private Uri mCapturedImageURI = null;

    static final int REQUEST_TAKE_PHOTO = 0;
    static final int REQUEST_GALLERY_PHOTO = 1;

    ImageView miImagen;
    FrameLayout llLayout;
    String mCurrentPhotoPath;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        llLayout = (FrameLayout) inflater.inflate(R.layout.fragment_subir_foto, container, false);

        miImagen = (ImageView) llLayout.findViewById(R.id.imageView1);
        open();
        return llLayout;
    }

    public void open(){
      Image_Picker_Dialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        QRCode qr= new QRCode(getActivity());
        switch(requestCode) {
            case REQUEST_TAKE_PHOTO://
                if(resultCode == Activity.RESULT_OK){
                    super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
                    //Bitmap bp = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    //miImagen.setImageBitmap(qr.unirQRImagen(bp, "String Test",4));

                    Bitmap miBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    //System.out.print("Width: " +miBitmap.getWidth() + ". Height" + miBitmap.getHeight());
                    miImagen.setImageBitmap(miBitmap);
                }

                break;
            case REQUEST_GALLERY_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    miImagen.setImageURI(selectedImage);
                }
                break;
        }

    }

    public void Image_Picker_Dialog()
    {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Subir fotografía");
        myAlertDialog.setMessage("Seleccione una opción");

        myAlertDialog.setPositiveButton("Galería", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
            }
        });

        myAlertDialog.setNegativeButton("Cámara", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                dispatchTakePictureIntent();//tambien llama al startActivityForResult
                //Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(takePicture, 0);//zero can be replaced with any action code
            }
        });
        myAlertDialog.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override public void onSaveInstanceState(Bundle savedInstanceState){
        if (mCurrentPhotoPath2 != null){
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        } if (mCapturedImageURI != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_URI_KEY, mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurrentPhotoPath2 = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
            mCapturedImageURI = Uri.parse(savedInstanceState.getString(CAPTURED_PHOTO_URI_KEY));
        }
    }

}
