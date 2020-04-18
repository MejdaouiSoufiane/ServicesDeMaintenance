package com.mejdaoui.servicesdemaintenance.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.mejdaoui.servicesdemaintenance.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyClusterManagerRenderer extends DefaultClusterRenderer<ClusterMarker> {
    private final IconGenerator iconGenerator;
    private ImageView imageView;
    private final int markerWidth;
    private final int markerHeight;

    public MyClusterManagerRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
        super(context, map, clusterManager);


        // initialize cluster item icon generator
        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        markerWidth = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        markerHeight = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWidth, markerHeight));
        int padding = (int) context.getResources().getDimension(R.dimen.custom_marker_padding);
        imageView.setPadding(padding, padding, padding, padding);
        iconGenerator.setContentView(imageView);
    }

    /**
     * Rendering of the individual ClusterItems
     * @param item
     * @param markerOptions
     */
    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
      //  Picasso.get().load(item.getIconPicture()).resize(50, 50).into(imageView);
        String drawableRes=item.getIconPicture();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(drawableRes);
            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }
        //imageView.setImageResource(R.drawable.image);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());

    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
        return false;
    }
}
