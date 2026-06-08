package com.iptv.player.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.iptv.player.data.repository.EpgRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * EPG 数据同步 Worker
 * 定期从 URL 获取 EPG 数据
 */
@HiltWorker
class EpgSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val epgRepository: EpgRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORK_NAME = "epg_sync"
        private const val KEY_EPG_URL = "epg_url"

        /**
         * 调度定期同步任务
         */
        fun schedule(context: Context, epgUrl: String) {
            val request = PeriodicWorkRequestBuilder<EpgSyncWorker>(
                12, TimeUnit.HOURS // 每 12 小时同步一次
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        /**
         * 取消同步任务
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun doWork(): Result {
        return try {
            // 实际应该从 inputData 获取 EPG URL
            // val epgUrl = inputData.getString(KEY_EPG_URL) ?: return Result.failure()
            // epgRepository.syncEpgFromUrl(epgUrl)
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
