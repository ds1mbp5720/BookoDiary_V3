package lee.project.core.database.room.uitl

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseUtil {
    // 모든 모듈에서 동일한 설정을 적용하기 위한 헬퍼 함수
    fun <T : RoomDatabase> createDatabase(
        context: Context,
        dbClass: Class<T>,
        dbName: String
    ): T {
        return Room.databaseBuilder(context, dbClass, dbName)
            .fallbackToDestructiveMigration() // 개발 편의를 위한 설정
            .build()
    }
}