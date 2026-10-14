package com.example.miniprojectnotebook.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.miniprojectnotebook.R
import com.example.miniprojectnotebook.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
           val email = binding.etUser.text.toString().trim()
           val password = binding.etPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter your email or password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.login(email,password)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result == "SUCCESS") {
                Toast.makeText(
                    requireContext(),
                    "Login Successful",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(
                    requireContext(),
                    result,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }
}