package com.dev.marcelo.devlanguages.features.auth.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.dev.marcelo.devlanguages.core.theme.Spacing
import com.dev.marcelo.devlanguages.core.theme.components.CustomTextField
import com.dev.marcelo.devlanguages.core.theme.components.LoadingIndicator
import com.dev.marcelo.devlanguages.core.theme.components.PrimaryButton
import com.dev.marcelo.devlanguages.core.theme.components.SecondaryButton
import com.dev.marcelo.devlanguages.core.utils.Platform
import devlanguages.composeapp.generated.resources.Res
import devlanguages.composeapp.generated.resources.apple
import devlanguages.composeapp.generated.resources.google
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * LoginScreen
 * Tela de login do aplicativo
 */
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    // Observar eventos de navegação
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.NavigateToHome -> onNavigateToHome()
                is LoginNavigationEvent.NavigateToSignUp -> onNavigateToSignUp()
            }
        }
    }

    // Mostrar erro em Snackbar
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(LoginEvent.ClearError)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.medium)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Bem-vindo!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Faça login para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.extraLarge))

            // Campo de Email
            CustomTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = "Email",
                placeholder = "seu@email.com",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                },
                isError = state.emailError != null,
                errorText = state.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            // Campo de Senha
            CustomTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = "Senha",
                placeholder = "Sua senha",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Senha"
                    )
                },
                isError = state.passwordError != null,
                errorText = state.passwordError,
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onEvent(LoginEvent.SignInWithEmail)
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) }) {
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
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.extraLarge))

            // Botão de Login
            if (state.isLoading) {
                LoadingIndicator()
            } else {
                PrimaryButton(
                    text = "Entrar",
                    onClick = { viewModel.onEvent(LoginEvent.SignInWithEmail) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Spacing.medium))

            // Botão de Cadastro
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Não tem uma conta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = { viewModel.onEvent(LoginEvent.NavigateToSignUp) },
                    enabled = !state.isLoading
                ) {
                    Text("Cadastre-se")
                }
            }

            Spacer(modifier = Modifier.height(Spacing.large))

            // Divider com "ou"
            Text(
                text = "ou",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Spacing.large))

            // Botões de login social (desabilitados por enquanto)
            SecondaryButton(
                text = "Entrar com Google",
                onClick = { viewModel.onEvent(LoginEvent.SignInWithGoogle) },
                iconPainter = painterResource(Res.drawable.google),
                enabled = false, // TODO: Implementar Google Sign-In
                modifier = Modifier.fillMaxWidth()
            )

            // Botão Apple aparece apenas no iOS
            if (Platform.isIOS) {
                Spacer(modifier = Modifier.height(Spacing.small))

                SecondaryButton(
                    text = "Entrar com Apple",
                    onClick = { viewModel.onEvent(LoginEvent.SignInWithApple) },
                    iconPainter = painterResource(Res.drawable.apple),
                    enabled = false, // TODO: Implementar Apple Sign-In
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Spacing.small))

            // Botão de login anônimo
            TextButton(
                onClick = { viewModel.onEvent(LoginEvent.SignInAnonymously) },
                enabled = !state.isLoading
            ) {
                Text("Continuar sem conta")
            }
        }
    }
}
