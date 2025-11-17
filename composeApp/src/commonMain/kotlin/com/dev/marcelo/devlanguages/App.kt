package com.dev.marcelo.devlanguages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dev.marcelo.devlanguages.core.di.appModules
import com.dev.marcelo.devlanguages.core.navigation.RootScreen
import com.dev.marcelo.devlanguages.core.navigation.Screen
import com.dev.marcelo.devlanguages.core.theme.AppTheme
import com.dev.marcelo.devlanguages.features.auth.ui.login.LoginScreen
import com.dev.marcelo.devlanguages.features.auth.ui.signup.SignUpScreen
import org.koin.compose.KoinApplication

/**
 * DevLanguages App
 * Entry point do aplicativo multiplataforma
 */
@Composable
fun App() {
    // Inicializar Koin com todos os modules
    KoinApplication(application = {
        modules(appModules)
    }) {
        AppTheme {
            AppNavigation()
        }
    }
}

/**
 * Navigation setup
 * Gerencia todas as rotas do aplicativo
 */
@Composable
private fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootScreen
    ) {
        // Root - Decide entre Login ou Home baseado em autenticação
        composable<RootScreen> {
            // TODO: Verificar se usuário está autenticado
            // Por enquanto, sempre navega para Login
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Login) {
                    popUpTo<RootScreen> { inclusive = true }
                }
            }
            PlaceholderScreen("Loading...")
        }

        // ===== Authentication Flow =====
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.Login> { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp)
                }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo<Screen.SignUp> { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ===== Onboarding Flow =====
        composable<Screen.Onboarding> {
            PlaceholderScreen("Onboarding Screen")
        }

        // ===== Main App Flow =====
        composable<Screen.Home> {
            PlaceholderScreen("Home Screen")
        }

        composable<Screen.Profile> {
            PlaceholderScreen("Profile Screen")
        }

        // ===== Game Generation Flow =====
        composable<Screen.Generating> {
            val args = it.toRoute<Screen.Generating>()
            PlaceholderScreen("Generating Screen\nPrompt: ${args.prompt}\nLanguage: ${args.language}")
        }

        composable<Screen.Explanation> {
            val args = it.toRoute<Screen.Explanation>()
            PlaceholderScreen("Explanation Screen\nSession: ${args.sessionId}")
        }

        // ===== Game Flow =====
        composable<Screen.GameContainer> {
            val args = it.toRoute<Screen.GameContainer>()
            PlaceholderScreen("Game Container\nSession: ${args.sessionId}")
        }

        composable<Screen.MatchingGame> {
            val args = it.toRoute<Screen.MatchingGame>()
            PlaceholderScreen("Matching Game\nGame: ${args.gameId}")
        }

        composable<Screen.TranslationGame> {
            val args = it.toRoute<Screen.TranslationGame>()
            PlaceholderScreen("Translation Game\nGame: ${args.gameId}")
        }

        composable<Screen.FillBlanksGame> {
            val args = it.toRoute<Screen.FillBlanksGame>()
            PlaceholderScreen("Fill Blanks Game\nGame: ${args.gameId}")
        }

        // ===== Results Flow =====
        composable<Screen.Score> {
            val args = it.toRoute<Screen.Score>()
            PlaceholderScreen("Score Screen\nSession: ${args.sessionId}")
        }

        // ===== Subscription Flow =====
        composable<Screen.Paywall> {
            PlaceholderScreen("Paywall Screen")
        }

        composable<Screen.Subscription> {
            PlaceholderScreen("Subscription Screen")
        }

        // ===== Settings Flow =====
        composable<Screen.Settings> {
            PlaceholderScreen("Settings Screen")
        }
    }
}

/**
 * Placeholder screen para desenvolvimento
 * Será substituída pelas telas reais conforme features forem implementadas
 */
@Composable
private fun PlaceholderScreen(title: String) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
