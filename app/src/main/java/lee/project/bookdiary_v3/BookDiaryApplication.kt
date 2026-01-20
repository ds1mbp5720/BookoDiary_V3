package lee.project.bookdiary_v3

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BookDiaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}