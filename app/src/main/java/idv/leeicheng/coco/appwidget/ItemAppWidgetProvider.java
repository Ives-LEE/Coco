package idv.leeicheng.coco.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.MainActivity;


public class ItemAppWidgetProvider extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            Intent intent = new Intent(context,QuicklyAddAcitivty.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_item);
            views.setOnClickPendingIntent(R.id.ibAppWidget,pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);

        }
    }
}
