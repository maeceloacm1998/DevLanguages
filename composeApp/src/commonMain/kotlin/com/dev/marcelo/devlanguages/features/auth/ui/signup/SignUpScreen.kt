package com.dev.marcelo.devlanguages.features.auth.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.dev.marcelo.devlanguages.core.theme.components.CustomTextField
import com.dev.marcelo.devlanguages.core.theme.components.LoadingIndicator
import com.dev.marcelo.devlanguages.core.theme.components.PrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar eventos de navegação
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                SignUpNavigationEvent.NavigateToHome -> onNavigateToHome()
                SignUpNavigationEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    // Mostrar erro no Snackbar
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(SignUpEvent.ClearError)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preencha os dados para começar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Nome de exibição
            CustomTextField(
                value = state.displayName,
                onValueChange = { viewModel.onEvent(SignUpEvent.DisplayNameChanged(it)) },
                label = "Nome de exibição",
                placeholder = "Como você quer ser chamado?",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nome de exibição"
                    )
                },
                isError = state.displayNameError != null,
                errorText = state.displayNameError,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            CustomTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
                label = "Email",
                placeholder = "seu@email.com",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.emailError != null,
                errorText = state.emailError,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Senha
            CustomTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
                label = "Senha",
                placeholder = "Mínimo 6 caracteres",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Senha"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(SignUpEvent.TogglePasswordVisibility) }
                    ) {
                        Icon(
                            imageVector = if (state.isPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (state.isPasswordVisible) {
                                "Ocultar senha"
                            } else {
                                "Mostrar senha"
                            }
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.passwordError != null,
                errorText = state.passwordError,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar senha
            CustomTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                label = "Confirmar senha",
                placeholder = "Digite a senha novamente",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirmar senha"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(SignUpEvent.ToggleConfirmPasswordVisibility) }
                    ) {
                        Icon(
                            imageVector = if (state.isConfirmPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (state.isConfirmPasswordVisible) {
                                "Ocultar senha"
                            } else {
                                "Mostrar senha"
                            }
                        )
                    }
                },
                visualTransformation = if (state.isConfirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.confirmPasswordError != null,
                errorText = state.confirmPasswordError,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão de cadastro
            PrimaryButton(
                text = "Criar Conta",
                onClick = { viewModel.onEvent(SignUpEvent.SignUp) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Link para login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Já tem uma conta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(4.dp))

                TextButton(
                    onClick = { viewModel.onEvent(SignUpEvent.NavigateToLogin) },
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = "Fazer Login",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Loading Indicator
        if (state.isLoading) {
            LoadingIndicator()
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
