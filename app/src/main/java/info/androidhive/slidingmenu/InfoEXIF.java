package info.androidhive.slidingmenu;

import android.media.ExifInterface;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
/**
 * Created by frutos on 27/10/2014.
 */
public class InfoEXIF {
    public static final String TAG = "InfoEXIF";
    public double latitud;
    public double longitud;
    public double altitud;
    public LatLng geopoint;

    public InfoEXIF(String jpgPath){
        try {
            ExifInterface exif = new ExifInterface(jpgPath);
            latitud = exif.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE, -1);
            longitud = exif.getAttributeDouble(ExifInterface.TAG_GPS_LONGITUDE, -1);
            altitud = exif.getAttributeDouble(ExifInterface.TAG_GPS_ALTITUDE, -1);

            geopoint = new LatLng(latitud, longitud);
        }
        catch (  Throwable t) {
            Log.w(TAG,"No se ha podido conseguir la información de: " + jpgPath);
        }
    }


    public static void setExifLatLon(String jpgPath, double latitud, double longitud){
        try {
            ExifInterface exif = new ExifInterface(jpgPath);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(latitud));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(longitud));
            exif.saveAttributes();
        }
        catch (  Throwable t) {
            Log.w(TAG, "No se ha podido establecer la información geográfica en: " + jpgPath);
        }
    }

    public double getLatitud(){
        return latitud;
    }
    public double getLongitud(){
        return longitud;
    }
    public double getAltitud(){
        return altitud;
    }
    public LatLng getEtiquetaGeografica(){
        return geopoint;
    }
}
