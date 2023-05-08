package com.studhub.app.data.local

import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.data.local.entity.UserFavoriteListings
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val localDatabase: LocalAppDatabase
) {
    private val userDao = localDatabase.userDao()
    private val conversationDao = localDatabase.conversationDao()
    private val messageDao = localDatabase.messageDao()
    private val listingDao = localDatabase.listingDao()
    private val userFavListingsDao = localDatabase.userFavListingsDao()

    suspend fun getUser(id: String): User? {
        return userDao.getUser(id)
    }

    suspend fun removeUser(id: String) {
        return userDao.deleteUser(id)
    }

    suspend fun saveUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getConversations(userId: String): List<Conversation> {
        return conversationDao.getConversations(userId)
    }

    suspend fun getConversation(conversationId: String): Conversation {
        return conversationDao.getConversation(conversationId)
    }

    suspend fun saveConversation(conversation: Conversation) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun getMessages(conversationId: String): List<Message> {
        return messageDao.getMessages(conversationId)
    }

    suspend fun saveMessage(message: Message) {
        messageDao.insertMessage(message)
    }

    suspend fun getListings(): List<Listing> {
        return listingDao.getListings()
    }

    suspend fun saveListing(listing: Listing) {
        listingDao.insertListing(listing)
    }

    suspend fun removeListing(id: String) {
        return listingDao.deleteListing(id)
    }

    suspend fun getFavListings(userId: String): List<Listing> {
        return userFavListingsDao.getUserFavorites(userId)
    }

    suspend fun saveFavoriteListing(userId: String, listing: Listing) {
        saveListing(listing)
        userFavListingsDao.insertFavoriteListing(UserFavoriteListings(userId, listing.id))
    }

    suspend fun removeFavoriteListing(userId: String, listingId: String) {
        removeListing(listingId)
        userFavListingsDao.deleteFavoriteListing(userId, listingId)
    }
}
