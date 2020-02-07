package com.example.voip_call;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker
{
    private static final String TAG = "MyWorker";
    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams)
    {
        super(appContext, workerParams);
    }
    @NonNull
    @Override
    public Result doWork()
    {
        Log.d(TAG, "Performing long running task in scheduled job");
        Toast.makeText(getApplicationContext(), "Working Notification" + Result.success(), Toast.LENGTH_SHORT).show();
        // TODO(developer): add long running task here.
        return Result.success();
    }
}