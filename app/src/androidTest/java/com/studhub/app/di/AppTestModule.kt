package com.studhub.app.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.data.NetworkStatusImplTest
import com.studhub.app.data.local.LocalDataSource
import com.studhub.app.data.local.database.LocalAppDatabase
import com.studhub.app.data.network.NetworkStatus
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
import com.studhub.app.presentation.listing.details.DetailedListingViewModel
import com.studhub.app.presentation.listing.details.IDetailedListingViewModel
import com.studhub.app.presentation.ratings.IUserRatingViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock
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

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalAppDatabase =
        Room.inMemoryDatabaseBuilder(
            context,
            LocalAppDatabase::class.java,
        ).build()

    @Provides
    fun provideLocalDatasource(localDatabase: LocalAppDatabase) = LocalDataSource(localDatabase)

    @Provides
    fun provideNetworkStatus(): NetworkStatus = NetworkStatusImplTest()

    @Singleton
    @Provides
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Singleton
    @Provides
    fun provideAuthRepository(): AuthRepository = MockAuthRepositoryImpl()

    @Singleton
    @Provides
    fun provideListingRepository(
        remoteDb: FirebaseDatabase,
        localDb: LocalDataSource,
        networkStatus: NetworkStatus
    ): ListingRepository = ListingRepositoryImpl(remoteDb, localDb, networkStatus)

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = MockUserRepositoryImpl()

    @Singleton
    @Provides
    fun provideConversationRepository(
        remoteDb: FirebaseDatabase,
        localDb: LocalDataSource,
        networkStatus: NetworkStatus
    ): ConversationRepository = ConversationRepositoryImpl(remoteDb, localDb, networkStatus)

    @Singleton
    @Provides
    fun provideMessageRepository(
        remoteDb: FirebaseDatabase,
        localDb: LocalDataSource,
        networkStatus: NetworkStatus
    ): MessageRepository = MessageRepositoryImpl(remoteDb, localDb, networkStatus)

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
        authRepository: AuthRepository
    ): SendMessage =
        SendMessage(messageRepository, authRepository)

    @Provides
    fun provideStartConversationWith(
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): StartConversationWith = StartConversationWith(conversationRepository, authRepository)

    @Singleton
    @Provides
    fun provideUserRatingViewModel(): IUserRatingViewModel = mock(IUserRatingViewModel::class.java)

    @Provides
    fun provideUserDao(localDatabase: LocalAppDatabase) = localDatabase.userDao()

    @Singleton
    @Provides
    fun provideDetailedListingViewModel(): IDetailedListingViewModel = mock(IDetailedListingViewModel::class.java)


}
