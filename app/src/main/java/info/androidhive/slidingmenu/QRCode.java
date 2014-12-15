package info.androidhive.slidingmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by frutos on 08/12/2014.
 */
public class QRCode {

    private Context mContex;

    public QRCode(Context context){
        mContex = context;
    }

    private Bitmap generarQRCode(String data, int w, int h)throws WriterException {


        com.google.zxing.Writer writer = new QRCodeWriter();
        String finaldata = Uri.encode(data, "utf-8");

        BitMatrix bm = writer.encode(finaldata, BarcodeFormat.QR_CODE,150, 150);
        Bitmap ImageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        try {
            for (int i = 0; i < w; i++) {//width
                for (int j = 0; j < h; j++) {//height
                    ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
                }
            }
        } catch (Exception e) {
            Log.w("QRCode", e.toString());
            Toast.makeText(mContex, "Error al generar el QR Code", Toast.LENGTH_SHORT).show();
        }

            return ImageBitmap;
    }
    public Bitmap unirQRImagen(Bitmap c, String datos, int posicion) {

        Bitmap s;


        Bitmap cs = null;

        int width, height, w  = 0;

        width = c.getWidth();
        height = c.getHeight();

        w = width / 10;

        try {
            s = generarQRCode(datos, w, w);
        } catch (WriterException e) {
            Log.w("QRCode - unirQRCode", e.toString());
            s = null;
        }

        float pos_x = 0;
        float pos_y = 0;

        switch (posicion){
            case 0:         //Posición ARRIBA - IZQUIERDA
                pos_x = 0;
                pos_y = 0;
                break;
            case 1:         //Posición ARRIBA - DERECHA
                pos_x = 0;
                pos_y = width - w + 10;
                break;
            case 2:         //Posición ABAJO - IZQUIERDA
                pos_x = height - w + 10;
                pos_y = 0;
                break;
            case 4:         //Posición ABAJO - DERECHA
                pos_x = height - w + 10;
                pos_y = width - w + 10;
                break;
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, new Matrix(), null);
        comboImage.drawBitmap(s, pos_x, pos_y, null);
        //comboImage.drawBitmap(s, new Matrix(), null);


        // Con el siguiente procedimiento se puede guardar el nuevo archivo.
        /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

        OutputStream os = null;
        try {
          os = new FileOutputStream(loc + tmpImg);
          cs.compress(CompressFormat.PNG, 100, os);
        } catch(IOException e) {
          Log.e("combineImages", "problem combining images", e);
        }*/

        return cs;
    }
}
