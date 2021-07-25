package uz.texnopos.instagram.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.instagram.data.helper.AuthHelper
import uz.texnopos.instagram.data.Settings
import uz.texnopos.instagram.data.helper.ProfileHelper
import uz.texnopos.instagram.ui.auth.signin.SignInViewModel
import uz.texnopos.instagram.ui.auth.signup.SignupViewModel
import uz.texnopos.instagram.ui.profile.ProfileViewModel
import uz.texnopos.instagram.ui.profile.edit.EditProfileViewModel

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { AuthHelper(get(), get()) }
    single { Settings(androidContext()) }
    single { ProfileHelper(get(), get()) }
}

val viewModelModule = module {
    viewModel { SignupViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
}
