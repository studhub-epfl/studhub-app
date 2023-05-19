package com.studhub.app.data.local

import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.data.local.entity.DraftListing
import com.studhub.app.data.local.entity.UnsentMessage
import com.studhub.app.data.local.entity.UserFavoriteListings
import com.studhub.app.domain.model.Conversation
import com.studhub.app.domain.model.Listing
import com.studhub.app.domain.model.Message
import com.studhub.app.domain.model.User
import kotlin.random.Random
import java.util.*
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val localDatabase: LocalAppDatabase
) {
    private val userDao = localDatabase.userDao()
    private val conversationDao = localDatabase.conversationDao()
    private val messageDao = localDatabase.messageDao()
    private val unsentMessageDao = localDatabase.unsentMessageDao()
    private val listingDao = localDatabase.listingDao()
    private val draftListingDao = localDatabase.draftListingDao()
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

    suspend fun saveUnsentMessage(message: Message) {
        val unsentMessage = UnsentMessage(
            id = 0,
            senderId = message.senderId,
            conversationId = message.conversationId,
            content = message.content,
            image = message.image,
            createdAt = message.createdAt
        )

        unsentMessageDao.insertUnsentMessage(unsentMessage)
    }

    suspend fun removeUnsentMessagesOfUser(userId: String) {
        unsentMessageDao.deleteUnsentMessages(userId)
    }

    suspend fun getUnsentMessagesOfConversation(conversationId: String): List<Message> {
        return unsentMessageDao.getUnsentMessages(conversationId).map {
            Message(
                senderId = it.senderId,
                conversationId = it.conversationId,
                content = it.content,
                image = it.image,
                createdAt = it.createdAt
            )
        }
    }

    suspend fun getUnsentMessagesOfUser(userId: String): List<Message> {
        return unsentMessageDao.getUnsentMessagesOfUser(userId).map {
            Message(
                senderId = it.senderId,
                conversationId = it.conversationId,
                content = it.content,
                image = it.image,
                createdAt = it.createdAt
            )
        }
    }

    suspend fun saveDraftListing(listing: Listing): Listing {
        val id = listing.id.ifEmpty { Random.nextLong().toString() }
        draftListingDao.insertDraftListing(
            DraftListing(
                id = id,
                sellerId = listing.sellerId,
                name = listing.name,
                description = listing.description,
                lastModifiedAt = Date(),
                price = listing.price,
                picturesUri = listing.picturesUri ?: emptyList(),
                type = listing.type,
                biddingDeadline = listing.biddingDeadline
            )
        )

        return listing.copy(id = id)
    }

    suspend fun getDraftListings(user: User): List<Listing> {
        return draftListingDao.getDraftListings(user.id).map {
            Listing(
                id = it.id,
                sellerId = it.sellerId,
                name = it.name,
                description = it.description,
                price = it.price,
                picturesUri = it.picturesUri,
                type = it.type,
                biddingDeadline = it.biddingDeadline
            )
        }
    }

    suspend fun getDraftListing(listingId: String): Listing? {
        val listing = draftListingDao.getDraftListing(listingId) ?: return null

        return Listing(
            id = listing.id,
            sellerId = listing.sellerId,
            name = listing.name,
            description = listing.description,
            price = listing.price,
            picturesUri = listing.picturesUri,
            type = listing.type,
            biddingDeadline = listing.biddingDeadline
        )
    }

    suspend fun removeDraftListing(listingId: String) {
        draftListingDao.deleteDraftListing(listingId)
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

    private suspend fun saveListing(listing: Listing) {
        listingDao.insertListing(listing)
    }

    private suspend fun removeListing(id: String) {
        return listingDao.deleteListing(id)
    }
}
