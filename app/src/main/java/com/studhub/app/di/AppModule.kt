package com.studhub.app.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.studhub.app.R
import com.studhub.app.core.Constants.SIGN_IN_REQUEST
import com.studhub.app.core.Constants.SIGN_UP_REQUEST
import com.studhub.app.data.repository.*
import com.studhub.app.domain.repository.*
import com.studhub.app.domain.usecase.category.GetCategories
import com.studhub.app.domain.usecase.category.GetCategory
import com.studhub.app.domain.usecase.conversation.GetConversationMessages
import com.studhub.app.domain.usecase.conversation.GetCurrentUserConversations
import com.studhub.app.domain.usecase.conversation.SendMessage
import com.studhub.app.domain.usecase.conversation.StartConversationWith
import com.studhub.app.domain.usecase.listing.*
import com.studhub.app.domain.usecase.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
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
    @Named(SIGN_IN_REQUEST)
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
    @Named(SIGN_UP_REQUEST)
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
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseDatabase
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
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
    fun provideSignOut(authRepository: AuthRepository): SignOut = SignOut(authRepository)

    @Provides
    fun provideCreateListing(listingRepository: ListingRepository): CreateListing =
        CreateListing(listingRepository)

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
