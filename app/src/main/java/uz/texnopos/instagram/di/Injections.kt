package uz.texnopos.instagram.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.instagram.data.helper.AuthHelper
import uz.texnopos.instagram.data.Settings
import uz.texnopos.instagram.data.helper.EditProfileHelper
import uz.texnopos.instagram.data.helper.PostHelper
import uz.texnopos.instagram.data.helper.ProfileHelper
import uz.texnopos.instagram.ui.add.AddPostViewModel
import uz.texnopos.instagram.ui.auth.signin.SignInViewModel
import uz.texnopos.instagram.ui.auth.signup.SignupViewModel
import uz.texnopos.instagram.ui.favorite.LikedPostViewModel
import uz.texnopos.instagram.ui.home.HomeViewModel
import uz.texnopos.instagram.ui.profile.ProfileViewModel
import uz.texnopos.instagram.ui.profile.edit.EditProfileViewModel

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { AuthHelper(get(), get()) }
    single { Settings(androidContext()) }
    single { FirebaseStorage.getInstance() }
    single { ProfileHelper(get(), get()) }
    single { PostHelper(get(), get(), get()) }
    single { EditProfileHelper(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { SignupViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { AddPostViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LikedPostViewModel(get()) }
}