package lee.project.data.book.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import lee.project.data.book.local.entity.MyBookEntity
import lee.project.data.book.local.entity.WishBookEntity

@Dao
interface BookDao {
    // WishBook 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishBook(book: WishBookEntity)

    @Query("SELECT * FROM wish_books")
    fun getAllWishBooks(): Flow<List<WishBookEntity>>

    @Query("SELECT * FROM wish_books WHERE title LIKE '%' || :query || '%'")
    fun searchWishBooks(query: String): Flow<List<WishBookEntity>>

    @Query("DELETE FROM wish_books WHERE itemId = :itemId")
    suspend fun deleteWishBookById(itemId: String)

    // MyBook 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyBook(book: MyBookEntity)

    @Query("SELECT * FROM my_books")
    fun getAllMyBooks(): Flow<List<MyBookEntity>>

    @Query("SELECT * FROM my_books WHERE title LIKE '%' || :query || '%'")
    fun searchMyBooks(query: String): Flow<List<MyBookEntity>>

    // MyBook 삭제
    @Query("DELETE FROM my_books WHERE itemId = :itemId")
    suspend fun deleteMyBookById(itemId: String)
}