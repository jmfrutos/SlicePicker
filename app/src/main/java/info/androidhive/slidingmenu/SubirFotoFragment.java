package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class SubirFotoFragment extends Fragment{
    ImageView miImagen;
    FrameLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        llLayout = (FrameLayout) inflater.inflate(R.layout.fragment_subir_foto, container, false);

        miImagen = (ImageView) llLayout.findViewById(R.id.imageView1);
        miImagen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

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
        QRCode qr= new QRCode(super.getActivity());
        switch(requestCode) {
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
                    Bitmap bp = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    miImagen.setImageBitmap(qr.unirQRImagen(bp, "String Test",4));
                }

                break;
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    miImagen.setImageURI(selectedImage);
                }
                break;
        }

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subir_foto_menu, menu);
        return true;
    }
*/
    public void Image_Picker_Dialog()
    {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Pictures Option");
        myAlertDialog.setMessage("Select Picture Mode");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });

        myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
            }
        });
        myAlertDialog.show();
    }

}
