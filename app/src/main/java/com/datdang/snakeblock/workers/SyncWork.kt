package com.datdang.snakeblock.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.datdang.snakeblock.domain.util.DispatchersProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

const val SYNC_WORK_MAX_ATTEMPTS = 3

@HiltWorker
class SyncWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val dispatchers: DispatchersProvider,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatchers.io) {
        // TODO: Implement sync logic if needed, e.g. upload best time to server
        // For now, just return success
        Log.d("SyncWork", "doWork() called -> success (no-op)")
        Result.success()
    }

    companion object {
        fun getOneTimeWorkRequest() = OneTimeWorkRequestBuilder<SyncWork>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    }
}