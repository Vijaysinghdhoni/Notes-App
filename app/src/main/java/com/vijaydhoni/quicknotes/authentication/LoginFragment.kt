package com.vijaydhoni.quicknotes.authentication

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.Identity
import com.vijaydhoni.quicknotes.R
import com.vijaydhoni.quicknotes.databinding.FragmentLoginBinding
import com.vijaydhoni.quicknotes.util.Constants
import com.vijaydhoni.quicknotes.util.setStatusBarColour
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }
    @Inject
     lateinit var sharedPreferences: SharedPreferences

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext().applicationContext,
            oneTapClient = Identity.getSignInClient(requireContext().applicationContext)
        )
    }

    private val launcher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    binding.progressBar.visibility = View.GONE
                    signInResult.data?.let {
                        sharedPreferences.edit().putBoolean(Constants.USER_LOGIN, true).apply()
                        findNavController().navigate(R.id.action_loginFragment_to_notesFragment)
                    }
                }
            } else {
                Log.d("tag", "registry for result is not ok")
                Log.d("tag", "Result code: ${result.resultCode}")

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColour(activity as AppCompatActivity, R.color.n_light_black)
        val isLogedin = sharedPreferences.getBoolean(Constants.USER_LOGIN, false)
        if (isLogedin) {
            findNavController().navigate(R.id.action_loginFragment_to_notesFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBttn.setOnClickListener {
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                val signInIntentSender = googleAuthUiClient.signIn()
                if (signInIntentSender == null) {
                    Log.d("tag", "intent sender is null")
                } else {
                    Log.d("tag", "intent sender is calling")
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender
                        ).build()
                    )
                }
            }
        }

    }


}