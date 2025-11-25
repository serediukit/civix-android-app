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
import org.serediukit.civix.models.entities.report.ReportStatus;

public class MarkerIconGenerator {

    public static BitmapDescriptor getMarkerIcon(Context context, ReportCategory category, ReportStatus status) {
        int categoryColor = getCategoryColor(context, category);
        int statusColor = getStatusColor(context, status);
        return createColoredMarkerIcon(context, categoryColor, statusColor);
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

    public static int getStatusColor(Context context, ReportStatus status) {
        int colorResId;
        switch (status) {
            case IN_PROGRESS:
                colorResId = R.color.status_in_progress;
                break;
            case COMPLETED:
                colorResId = R.color.status_completed;
                break;
            case REJECTED:
                colorResId = R.color.status_rejected;
                break;
            case CANCELED:
                colorResId = R.color.status_canceled;
                break;
            case NEW:
            default:
                colorResId = R.color.status_new;
                break;
        }
        return ContextCompat.getColor(context, colorResId);
    }

    private static BitmapDescriptor createColoredMarkerIcon(Context context, int categoryColor, int statusColor) {
        // Create a bitmap from the drawable
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_custom_marker);
        if (vectorDrawable == null) {
            return BitmapDescriptorFactory.defaultMarker();
        }

        int width = 120;  // Marker width in pixels
        int height = 120; // Marker height in pixels

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw the base marker shape
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        // Color the marker body with status color
        Paint bodyPaint = new Paint();
        bodyPaint.setColor(statusColor);
        bodyPaint.setAntiAlias(true);
        bodyPaint.setStyle(Paint.Style.FILL);

        // Draw the marker body shape (matching the path in ic_custom_marker.xml)
        android.graphics.Path markerPath = new android.graphics.Path();
        float scale = width / 48f; // Scale from viewportWidth to actual width

        markerPath.moveTo(24 * scale, 4 * scale);
        markerPath.cubicTo(16.268f * scale, 4 * scale, 10 * scale, 10.268f * scale, 10 * scale, 18 * scale);
        markerPath.cubicTo(10 * scale, 27.75f * scale, 24 * scale, 44 * scale, 24 * scale, 44 * scale);
        markerPath.cubicTo(24 * scale, 44 * scale, 38 * scale, 27.75f * scale, 38 * scale, 18 * scale);
        markerPath.cubicTo(38 * scale, 10.268f * scale, 31.732f * scale, 4 * scale, 24 * scale, 4 * scale);
        markerPath.close();

        canvas.drawPath(markerPath, bodyPaint);

        // Draw the colored circle on top for category
        Paint circlePaint = new Paint();
        circlePaint.setColor(categoryColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        // Draw circle centered in the marker body
        // In the drawable: circle is at Y=18 in a viewportHeight of 48 (37.5%)
        // Circle radius is 7 in a viewportWidth of 48 (14.6%)
        float centerX = width / 2f;
        float centerY = height * 0.375f; // Position at Y=18/48 = 37.5%
        float radius = width * 0.146f;   // Radius = 7/48 = 14.6%

        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw border around the circle
        circlePaint.setColor(0xFF333333);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(2f);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Redraw the marker border on top
        Paint borderPaint = new Paint();
        borderPaint.setColor(0xFF333333);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1.5f * scale);
        borderPaint.setAntiAlias(true);
        canvas.drawPath(markerPath, borderPaint);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
