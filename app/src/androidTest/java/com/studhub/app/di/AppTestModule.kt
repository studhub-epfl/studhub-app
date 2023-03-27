package com.studhub.app.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.R
import com.studhub.app.core.Constants
import com.studhub.app.data.repository.*
import com.studhub.app.domain.repository.*
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.listing.*
import com.studhub.app.domain.usecase.user.CreateUser
import com.studhub.app.domain.usecase.user.GetCurrentUser
import com.studhub.app.domain.usecase.user.GetUser
import com.studhub.app.domain.usecase.user.UpdateUser
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
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
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ): SignInClient = Identity.getSignInClient(context)

    @Provides
    @Named(Constants.SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(Constants.SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Singleton
    @Provides
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Singleton
    @Provides
    fun provideAuthRepository(): AuthRepository = MockAuthRepositoryImpl()

    @Singleton
    @Provides
    fun provideListingRepository(): ListingRepository = MockListingRepositoryImpl()

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = MockUserRepositoryImpl()

    @Singleton
    @Provides
    fun provideConversationRepository(): ConversationRepository = ConversationRepositoryImpl()

    @Singleton
    @Provides
    fun provideMessageRepository(): MessageRepository = MessageRepositoryImpl()

    @Provides
    fun provideCreateUser(userRepository: UserRepository): CreateUser = CreateUser(userRepository)

    @Provides
    fun provideGetCurrentUserUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository
    ): GetCurrentUser = GetCurrentUser(userRepository, authRepository)

    @Provides
    fun provideGetUser(userRepository: UserRepository): GetUser = GetUser(userRepository)

    @Provides
    fun provideUpdateUser(userRepository: UserRepository): UpdateUser = UpdateUser(userRepository)

    @Provides
    fun provideCreateListing(listingRepository: ListingRepository): CreateListing = CreateListing(listingRepository)

    @Provides
    fun provideGetListing(listingRepository: ListingRepository): GetListing = GetListing(listingRepository)

    @Provides
    fun provideGetListings(listingRepository: ListingRepository): GetListings = GetListings(listingRepository)

    @Provides
    fun provideRemoveListing(listingRepository: ListingRepository): RemoveListing = RemoveListing(listingRepository)

    @Provides
    fun provideUpdateListing(listingRepository: ListingRepository): UpdateListing = UpdateListing(listingRepository)

    @Provides
    fun provideGetCategories(categoryRepository: CategoryRepository): GetCategories = GetCategories(categoryRepository)

    @Provides
    fun provideGetCategory(categoryRepository: CategoryRepository): GetCategory = GetCategory(categoryRepository)

    @Provides
    fun provideGetConversationMessages(messageRepository: MessageRepository): GetConversationMessages =
        GetConversationMessages(messageRepository)

    @Provides
    fun provideGetCurrentUserConversations(
        conversationRepository: ConversationRepository,
        authRepository: AuthRepository
    ): GetCurrentUserConversations =
        GetCurrentUserConversations(conversationRepository, authRepository)
}
