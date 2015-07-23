package org.wultimaproject.db2.services;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.wultimaproject.db2.R;
import org.wultimaproject.db2.SettingTourActivity;
import org.wultimaproject.db2.ShowTourTimeLineActivity;
import org.wultimaproject.db2.structures.Constants;
import org.wultimaproject.db2.utils.Repository;
import org.wultimaproject.db2.structures.Site;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by antoniocoppola on 11/06/15.
 */
public class TourAlgorithmTask extends AsyncTask<Void, Void, ArrayList<Site>> {

    ShowTourTimeLineActivity showTourTimeLineActivityInstance;
    static WeakReference<ShowTourTimeLineActivity> showTourTimeLineActivityWeakReference;
    String algToUse="";
    Site newSite;



    Context context=SettingTourActivity.context;
//    Gson gson=new Gson();
//    Type type=new TypeToken<Site>() {}.getType();
//    String json= Repository.retrieve(context, Constants.STARTING_SITE, String.class);
//    Site newSite=gson.fromJson(json,type);


    Gson gsonCalendar=new Gson();
    Type typeCalendar=new TypeToken<Calendar>(){}.getType();
    String jSonCalendar=Repository.retrieve(context, Constants.WHEN_SAVE, String.class);
    Calendar actualDate=gsonCalendar.fromJson(jSonCalendar,typeCalendar);


    public TourAlgorithmTask(ShowTourTimeLineActivity wShowTourTimeLineActivity, String algortithmToUse){
        showTourTimeLineActivityWeakReference=new WeakReference<ShowTourTimeLineActivity>(wShowTourTimeLineActivity);
        algToUse=algortithmToUse;
        newSite= new Site();

        //todo if the GPS failed, Double.valueOf tries to convert "" in Double, which cause crash.

        //checking if the result from GPS is valid or not

        newSite.latitude=Double.valueOf(Repository.retrieve(context,Constants.LATITUDE_STARTING_POINT,String.class));
        newSite.longitude=Double.valueOf(Repository.retrieve(context,Constants.LONGITUDE_STARTING_POINT,String.class));

        newSite.name="this actual position";
        Log.d("miotag","Before TourAlgorith, the coordinates are: "+newSite.latitude+", "+newSite.longitude);

    }

    @Override
    protected ArrayList<Site> doInBackground(Void... params){

        return TourAlgorithm.showTour(newSite, Float.valueOf(Repository.retrieve(context, Constants.TIME_TO_SPEND, String.class)), context, actualDate, algToUse);
    }

    @Override
    protected void onPostExecute(ArrayList<Site> siteReturning){


        showTourTimeLineActivityInstance=showTourTimeLineActivityWeakReference.get();


            if (TextUtils.equals(siteReturning.get(0).name,"dummy")){
                Log.d("miotag","DUMMY! relaunch screen mode: ON!");
                 if (showTourTimeLineActivityInstance != null){
                     showTourTimeLineActivityInstance.showDummyActivity();
                 }

            } else if (showTourTimeLineActivityInstance != null){
            showTourTimeLineActivityInstance.siteToStamp=siteReturning;

//            showTourTimeLineActivityInstance.ll1=  (LinearLayout) showTourTimeLineActivityInstance.findViewById(R.id.llShowTimeLine);
//            showTourTimeLineActivityInstance.ll1.setBackgroundColor(context.getResources().getColor(R.color.white));


            showTourTimeLineActivityInstance.progressBar=(ProgressBar)  showTourTimeLineActivityInstance.findViewById(R.id.progressBarTourTimeLine);
            showTourTimeLineActivityInstance.progressBar.setVisibility(View.GONE);
            showTourTimeLineActivityInstance.showResultFromAlgorithm();
        }else {
            Log.d("miotag","wakReference is dead");
            showTourTimeLineActivityWeakReference = new WeakReference<>(showTourTimeLineActivityInstance);

        }

    }

}



