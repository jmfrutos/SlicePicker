package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Roy Ramos
 * 
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

	public GridViewAdapter(Context context, int layoutResourceId,
			ArrayList<ImageItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View row;
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.text);
			holder.image = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			row = convertView;
			holder = (ViewHolder) row.getTag();
		}

		final ImageItem item = data.get(position);
		holder.imageTitle.setText(item.getTitle());
		holder.image.setImageBitmap(item.getImage());
		
		
		//Now get the id or whatever needed
	    row.setId(position);
	    // Now set the onClickListener
	    row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Toast.makeText(context, "Imagen #" + row.getId() + ".", Toast.LENGTH_SHORT).show();
			}

		});

	    row.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//Creating the instance of PopupMenu
				//Toast.makeText(context, "POPUP MENU", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, row);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.recientes_list_menu, popup.getMenu());
				
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item2) {
                    	final Bitmap myImage = item.getImage();
                    	new Thread(new Runnable(){
        					public void run(){
        						saveImage(myImage);		
        					}
        				}).start();
        				Toast.makeText(context, "Imagen guardada!", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, "You Clicked : " + item2.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                
                popup.show();
                

				return true;
			}

		});		
		return row;
	}

	static class ViewHolder {
		TextView imageTitle;
		ImageView image;
	}
	
	private void saveImage(Bitmap finalBitmap) {

	    String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/SlicePicker");
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-"+ n +".jpg";
	    File file = new File(myDir, fname);
	    if (file.exists ()) file.delete (); 
	    try {
	           FileOutputStream out = new FileOutputStream(file);
	           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	           out.flush();
	           out.close();
	           context.sendBroadcast(new Intent(
	        		   Intent.ACTION_MEDIA_MOUNTED,
	        		               Uri.parse("file://" + Environment.getExternalStorageDirectory())));

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	    
	}
}