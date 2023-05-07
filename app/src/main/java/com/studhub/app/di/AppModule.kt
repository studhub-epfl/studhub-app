package com.studhub.app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.data.network.NetworkStatus
import com.studhub.app.data.network.NetworkStatusImpl
import com.studhub.app.data.repository.*
import com.studhub.app.domain.repository.*
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import com.studhub.app.domain.usecase.conversation.*
import com.studhub.app.domain.usecase.listing.*
import com.studhub.app.domain.usecase.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseDatabase() = Firebase.database

    @Provides
    @Singleton
    fun provideNetworkStatus(@ApplicationContext context: Context): NetworkStatus =
        NetworkStatusImpl(context)

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        db: FirebaseDatabase
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        db = db
    )

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = UserRepositoryImpl()

    @Singleton
    @Provides
    fun provideListingRepository(): ListingRepository = ListingRepositoryImpl()

    @Singleton
    @Provides
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Singleton
    @Provides
    fun provideConversationRepository(): ConversationRepository = ConversationRepositoryImpl()

    @Singleton
    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()

    @Provides
    fun provideGetCurrentUserUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository
    ): GetCurrentUser = GetCurrentUser(userRepository, authRepository)

    @Provides
    fun provideGetUser(userRepository: UserRepository): GetUser = GetUser(userRepository)

    @Provides
    fun provideUpdateCurrentUserInfo(
        userRepository: UserRepository,
        authRepository: AuthRepository
    ): UpdateCurrentUserInfo = UpdateCurrentUserInfo(userRepository, authRepository)

    @Provides
    fun provideSignOut(authRepository: AuthRepository): SignOut = SignOut(authRepository)

    @Provides
    fun provideCreateListing(
        listingRepository: ListingRepository,
        authRepository: AuthRepository
    ): CreateListing =
        CreateListing(listingRepository, authRepository)

    @Provides
    fun provideGetListing(listingRepository: ListingRepository): GetListing =
        GetListing(listingRepository)

    @Provides
    fun provideGetListings(listingRepository: ListingRepository): GetListings =
        GetListings(listingRepository)

    @Provides
    fun provideRemoveListing(listingRepository: ListingRepository): RemoveListing =
        RemoveListing(listingRepository)

    @Provides
    fun provideUpdateListing(listingRepository: ListingRepository): UpdateListing =
        UpdateListing(listingRepository)

    @Provides
    fun provideGetListingsBySearch(
        repository: ListingRepository,
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): GetListingsBySearch =
        GetListingsBySearch(repository, authRepository, userRepository)

    @Provides
    fun provideGetCategories(categoryRepository: CategoryRepository): GetCategories =
        GetCategories(categoryRepository)

    @Provides
    fun provideGetCategory(categoryRepository: CategoryRepository): GetCategory =
        GetCategory(categoryRepository)

    @Provides
    fun provideGetConversation(
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): GetConversation =
        GetConversation(conversationRepository, authRepository)

    @Provides
    fun provideGetConversationMessages(messageRepository: MessageRepository): GetConversationMessages =
        GetConversationMessages(messageRepository)

    @Provides
    fun provideGetCurrentUserConversations(
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): GetCurrentUserConversations =
        GetCurrentUserConversations(conversationRepository, authRepository)

    @Provides
    fun provideSendMessage(
        messageRepository: MessageRepository,
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): SendMessage =
        SendMessage(messageRepository, conversationRepository, authRepository)

    @Provides
    fun provideStartConversationWith(
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): StartConversationWith = StartConversationWith(conversationRepository, authRepository)
    @Provides
    fun provideAddRating(userRepository: UserRepository): AddRating =
        AddRating(userRepository)
    @Provides
    fun provideDeleteRating(userRepository: UserRepository): DeleteRating =
        DeleteRating(userRepository)

    @Provides
    fun provideUpdateRating(userRepository: UserRepository): UpdateRating =
        UpdateRating(userRepository)

    @Provides
    fun provideGetUserRatings(userRepository: UserRepository): GetUserRatings =
        GetUserRatings(userRepository)


}
