package tcc.ebuild_slider;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by momberg on 25/02/16.
 */
public class LoggedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{

    protected static final String TAG = "APP";

    private GoogleMap mMap;
    FloatingActionButton fab_menu, fabAction1, fabAction2, fabAction3;
    private boolean expanded = false;
    boolean preenchido = false, adicionado = false;
    private float offset1, offset2, offset3;
    double lat, lng;
    String int_ext, item_selecionado, nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra;
    Obra obra = new Obra();
    SharedPreferences cod_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ObrasDB db = new ObrasDB(this);
        cod_final = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preenchido = false;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ViewGroup fabContainer = (ViewGroup) findViewById(R.id.fab_container);
        fabAction1 = (FloatingActionButton) findViewById(R.id.fab1);
        fabAction2 = (FloatingActionButton) findViewById(R.id.fab2);
        fabAction3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expanded = !expanded;
                if (expanded) {
                    expandFab();
                } else {
                    collapseFab();
                }
            }
        });

        fabAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObrasDB db = new ObrasDB(getApplicationContext());
                db.limpa_db();
            }
        });

        fabAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obra.getMarker() && (!preenchido)) {
                    Intent intent = new Intent(view.getContext(), FormActivity.class);
                    startActivity(intent);
                    adicionado = false;
                } else if ((!adicionado) && (preenchido)){
                    toast("Informação ja adicionada");
                } else {
                    toast("Adicione um marcador");
                }
            }
        });

        fabAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preenchido = cod_final.getBoolean("preenchido", preenchido);
                if(preenchido) {
                    nome_obra = cod_final.getString("nome", "");
                    data_obra = cod_final.getString("data", "");
                    rua_obra = cod_final.getString("rua", "");
                    bairro_obra = cod_final.getString("bairro", "");
                    cidade_obra = cod_final.getString("cidade", "");
                    int_ext = cod_final.getString("tipo", "");
                    item_selecionado = cod_final.getString("fase", "");
                    obra.setObraOnClick(nome_obra, data_obra, rua_obra, bairro_obra, cidade_obra, int_ext, item_selecionado, lat, lng);
                    db.save(obra);
                    preenchido = false;
                    cod_final.edit().putBoolean("preenchido", preenchido).apply();
                    fabAction3.setVisibility(View.INVISIBLE);
                    obra.setMarker(false);
                    adicionado = true;
                } else {
                    toast("Informações não foram adicionadas");
                }
            }
        });

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fab_menu.getY() - fabAction1.getY();
                fabAction1.setTranslationY(offset1);
                offset2 = fab_menu.getY() - fabAction2.getY();
                fabAction2.setTranslationY(offset2);
                offset3 = fab_menu.getY() - fabAction3.getY();
                fabAction3.setTranslationY(offset3);
                return true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DrawnMarkers(mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                if(!obra.getMarker()) {
                    mMap.addMarker(new MarkerOptions().position(point).draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    lat = point.latitude;
                    lng = point.longitude;
                    obra.setMarker(true);
                } else {
                    toast("Adicione apenas um marcador por vez");
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        preenchido = cod_final.getBoolean("preenchido", preenchido);
        if(!preenchido){
            fabAction3.setVisibility(View.INVISIBLE);
        } else {
            fabAction3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.map_hib) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.map_nor) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.map_sat) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.logout) {
            this.finish();
        } else if (id == R.id.works) {
            DialogCall dialog = new DialogCall();
            dialog.call_Dialog_Lista_Obras_logged(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void collapseFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabAction1, offset1),
                createCollapseAnimator(fabAction2, offset2),
                createCollapseAnimator(fabAction3, offset3));
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabAction1, offset1),
                createExpandAnimator(fabAction2, offset2),
                createExpandAnimator(fabAction3, offset3));
        animatorSet.start();
        animateFab();
    }

    private static final String TRANSLATION_Y = "translationY";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    private void animateFab() {
        Drawable drawable = fab_menu.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    public void DrawnMarkers(GoogleMap googlemap){
        ObraService service = new ObraService();
        Double lat, lng;
        try {
            List<Obra> obras = service.getObras(this);
            for(Obra obra:obras){
                lat = obra.getLat();
                lng = obra.getLng();
                LatLng point = new LatLng(lat, lng);
                googlemap.addMarker(new MarkerOptions().position(point).draggable(false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toast(String text){
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }
}
