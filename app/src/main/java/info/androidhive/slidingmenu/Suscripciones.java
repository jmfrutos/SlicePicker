package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.sql.SQLException;


public class Suscripciones extends ListFragment {

    private SuscripcionesSQLiteHelper dbHelper;
    private SimpleCursorAdapter dataAdapter;

    private LinearLayout ll;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ll = (LinearLayout) inflater.inflate(R.layout.activity_suscripciones, container, false);

        dbHelper = new SuscripcionesSQLiteHelper(super.getActivity());

        try {
            dbHelper.open();
        } catch (SQLException e){
            Log.w(SuscripcionesSQLiteHelper.TAG, e.toString());
        }

        dbHelper.borrarSuscripciones();
        dbHelper.insertSuscripcionesMuestra();


        Cursor cursor = dbHelper.fetchAllSuscripciones();
        String[] columns = new String[] {
                SuscripcionesSQLiteHelper.KEY_ID,
                SuscripcionesSQLiteHelper.KEY_SUSCRIPCION,
                SuscripcionesSQLiteHelper.KEY_DESCRIPCION,
                SuscripcionesSQLiteHelper.KEY_MASINFO
        };

        int[] to =  new int[] {
                R.id.tvID,
                R.id.tvSuscripcion,
                R.id.tvDescripcion,
                R.id.tvMasInfo,
        };

        dataAdapter = new SimpleCursorAdapter(super.getActivity(), R.layout.suscripcion_detalle,
                cursor, columns, to, 0);
        setListAdapter(dataAdapter);

        EditText filtro = (EditText) ll.findViewById(R.id.filtro);
        filtro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                dataAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Cursor cursor = null;
                try {
                    cursor =  dbHelper.fetchSuscripcionesPorNombre(charSequence.toString());
                }
                catch (SQLException e){
                    Log.w(SuscripcionesSQLiteHelper.TAG, e.toString());
                }
                return cursor;
            }
        });

        return inflater.inflate(R.layout.activity_suscripciones, container, false);
        //return ll;

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Toast.makeText(super.getActivity(),
                "Clicked " + getListAdapter().getItem(position).toString(),
                Toast.LENGTH_SHORT).show();
    }

    public void refrescarLista(){
        Cursor cursor = dbHelper.fetchAllSuscripciones();
        String[] columns = new String[] {
                SuscripcionesSQLiteHelper.KEY_ID,
                SuscripcionesSQLiteHelper.KEY_SUSCRIPCION,
                SuscripcionesSQLiteHelper.KEY_DESCRIPCION,
                SuscripcionesSQLiteHelper.KEY_MASINFO
        };

        int[] to =  new int[] {
                R.id.tvID,
                R.id.tvSuscripcion,
                R.id.tvDescripcion,
                R.id.tvMasInfo,
        };

        dataAdapter = new SimpleCursorAdapter(super.getActivity(), R.layout.suscripcion_detalle,
                cursor, columns, to, 0);
        setListAdapter(dataAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);



        ListView listView = getListView();
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(final ListView listView, final int[] reverseSortedPositions) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Suscripciones.super.getActivity());
                                alertDialog.setMessage("¿Desea eliminar esta suscripción?");
                                alertDialog.setTitle("Eliminar Suscripción");
                                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Borrar en la BD
                                        //dataAdapter.remove(dataAdapter.getItem(position));
                                        for (final int position : reverseSortedPositions) {
                                            try {
                                                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                                dbHelper.deleteContacto(cursor.getString(cursor.getColumnIndexOrThrow(SuscripcionesSQLiteHelper.KEY_ID)));

                                                //listView.invalidateViews();
                                                refrescarLista();
                                                //Suscripciones.this.recreate(); //TEMPORAL NO ES LO MEJOR!!
                                                Toast.makeText(Suscripciones.super.getActivity(), "Suscripcion Eliminada", Toast.LENGTH_SHORT).show();
                                            } catch (SQLException e){
                                                Log.w(SuscripcionesSQLiteHelper.TAG, e.toString());
                                            }
                                        }
                                    }
                                });
                                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //código java si se ha pulsado no
                                    }
                                });
                                alertDialog.show();
                                dataAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

/*        // Set up normal ViewGroup example
        final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
        for (int i = 0; i < items.length; i++) {
            final Button dismissableButton = new Button(this);
            dismissableButton.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dismissableButton.setText("Button " + (i + 1));
            dismissableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Suscripciones.this,
                            "Clicked " + ((Button) view).getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            // Create a generic swipe-to-dismiss touch listener.
            dismissableButton.setOnTouchListener(new SwipeDismissTouchListener(
                    dismissableButton,
                    null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            dismissableContainer.removeView(dismissableButton);
                        }
                    }));
            dismissableContainer.addView(dismissableButton);
        }*/
    }

}
