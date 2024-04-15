package com.devexpert.workmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button workerBtn = findViewById(R.id.workerBtn);

        Data data = new Data.Builder()
                .putInt("max_limit", 500).build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        workerBtn.setOnClickListener(listener -> {
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
        });

        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null) {
                        Toast.makeText(MainActivity.this,
                                "Status" + workInfo.getState().name(),
                                Toast.LENGTH_SHORT).show();
                    }

                    if (workInfo.getState().isFinished()) {
                        Data outputData = workInfo.getOutputData();

                        Toast.makeText(MainActivity.this,
                                        "" + outputData.getString("message"), Toast.LENGTH_SHORT)
                                .show();
                        ;
                    }
                });
    }
}