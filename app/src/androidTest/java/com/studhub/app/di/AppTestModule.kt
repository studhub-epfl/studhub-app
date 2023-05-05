package com.studhub.app.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.data.repository.*
import com.studhub.app.domain.repository.*
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.conversation.SendMessage
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import com.studhub.app.domain.usecase.listing.*
import com.studhub.app.domain.usecase.user.GetCurrentUser
import com.studhub.app.domain.usecase.user.GetUser
import com.studhub.app.domain.usecase.user.SignOut
import com.studhub.app.domain.usecase.user.UpdateCurrentUserInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
class AppTestModule {
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseDatabase() = Firebase.database

    @Singleton
    @Provides
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Singleton
    @Provides
    fun provideAuthRepository(): AuthRepository = MockAuthRepositoryImpl()

    @Singleton
    @Provides
    fun provideListingRepository(): ListingRepository = ListingRepositoryImpl()

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = MockUserRepositoryImpl()

    @Singleton
    @Provides
    fun provideConversationRepository(): ConversationRepository = ConversationRepositoryImpl()

    @Singleton
    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()

    @Singleton
    @Provides
    fun provideReportRepository(): ReportRepository = ReportRepositoryImpl()

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
}
