package lee.project.data.book.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lee.project.data.book.local.dao.BookDao
import lee.project.data.book.local.entity.MyBookEntity
import lee.project.data.book.local.entity.WishBookEntity

@Database(
    entities = [
        WishBookEntity::class,
        MyBookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}