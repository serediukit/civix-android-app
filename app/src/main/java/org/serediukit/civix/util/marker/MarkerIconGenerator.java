package org.serediukit.civix.util.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.serediukit.civix.R;
import org.serediukit.civix.models.entities.report.ReportCategory;

public class MarkerIconGenerator {

    public static BitmapDescriptor getMarkerIcon(Context context, ReportCategory category) {
        int color = getCategoryColor(context, category);
        return createColoredMarkerIcon(context, color);
    }

    public static int getCategoryColor(Context context, ReportCategory category) {
        int colorResId;
        switch (category) {
            case ROAD:
                colorResId = R.color.category_road;
                break;
            case SIDEWAY:
                colorResId = R.color.category_sideway;
                break;
            case ELECTRIC:
                colorResId = R.color.category_electric;
                break;
            case WATER:
                colorResId = R.color.category_water;
                break;
            case GAS:
                colorResId = R.color.category_gas;
                break;
            case HEAT:
                colorResId = R.color.category_heat;
                break;
            case UNKNOWN:
            default:
                colorResId = R.color.category_unknown;
                break;
        }
        return ContextCompat.getColor(context, colorResId);
    }

    private static BitmapDescriptor createColoredMarkerIcon(Context context, int color) {
        // Create a bitmap from the drawable
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_custom_marker);
        if (vectorDrawable == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        int width = 120;  // Marker width in pixels
        int height = 120; // Marker height in pixels

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        // Draw the colored circle on top
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        // Draw circle centered in the marker body
        // In the drawable: circle is at Y=18 in a viewportHeight of 48 (37.5%)
        // Circle radius is 7 in a viewportWidth of 48 (14.6%)
        float centerX = width / 2f;
        float centerY = height * 0.375f; // Position at Y=18/48 = 37.5%
        float radius = width * 0.146f;   // Radius = 7/48 = 14.6%

        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw border around the circle
        paint.setColor(0xFF333333);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        canvas.drawCircle(centerX, centerY, radius, paint);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
