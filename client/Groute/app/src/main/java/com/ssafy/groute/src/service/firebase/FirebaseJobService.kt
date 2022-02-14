package com.ssafy.groute.src.service.firebase

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class FirebaseJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("JobService_Groute", "Performing long running task in scheduled job")
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}