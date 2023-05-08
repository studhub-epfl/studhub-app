package com.studhub.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studhub.app.data.local.dao.ConversationDao
import com.studhub.app.data.local.dao.ListingDao
import com.studhub.app.data.local.dao.MessageDao
import com.studhub.app.data.local.dao.UserDao
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User

@Database(
    entities = [User::class, Conversation::class, Message::class, Listing::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun listingDao(): ListingDao
}
