package info.androidhive.slidingmenu;

import java.util.ArrayList;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class recientesFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    RelativeLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        llLayout = (RelativeLayout) inflater.inflate(R.layout.recientes_main, container, false);
        gridView = (GridView) llLayout.findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(getActivity(), R.layout.recientes_row_grid, getData());
        gridView.setAdapter(customGridAdapter);

		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(MainActivity.this, position + "#Selected",
						Toast.LENGTH_SHORT).show();
			}
		});
		*/

        /*gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                Toast.makeText(RecientesActivity.this, "LONG PRESS", Toast.LENGTH_SHORT).show();
                //set the image as wallpaper

            }
        });
        */
        return llLayout;
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

    @SuppressLint("Recycle")
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        // retrieve String drawable array
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }

        return imageItems;

    }

}