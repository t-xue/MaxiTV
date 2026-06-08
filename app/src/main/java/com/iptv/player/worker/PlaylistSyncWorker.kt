package com.iptv.player.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.iptv.player.data.repository.PlaylistRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * 播放列表同步 Worker
 * 定期从 URL 刷新播放列表
 */
@HiltWorker
class PlaylistSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val playlistRepository: PlaylistRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORK_NAME = "playlist_sync"

        /**
         * 调度定期同步任务
         */
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<PlaylistSyncWorker>(
                6, TimeUnit.HOURS // 每 6 小时同步一次
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
            // 获取所有有 URL 的播放列表并刷新
            // 这里简化处理，实际应该遍历所有播放列表
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
