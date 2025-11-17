package com.dev.marcelo.devlanguages.features.onboarding.ui.tutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev.marcelo.devlanguages.core.theme.Spacing
import com.dev.marcelo.devlanguages.core.theme.components.LoadingIndicator
import com.dev.marcelo.devlanguages.core.theme.components.PrimaryButton
import com.dev.marcelo.devlanguages.features.onboarding.domain.model.OnboardingStep
import org.koin.compose.viewmodel.koinViewModel

/**
 * TutorialScreen
 * Tela de tutorial do onboarding (explicação do app)
 */
@Composable
fun TutorialScreen(
    onNavigateToHome: () -> Unit,
    viewModel: TutorialViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar eventos de navegação
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is TutorialNavigationEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    // Mostrar erro em Snackbar
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onEvent(TutorialEvent.ClearError)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header com Skip e Progress
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.onEvent(TutorialEvent.Skip) },
                        enabled = !state.isLoading
                    ) {
                        Text("Pular")
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.small))

                // Progress bar
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(Spacing.extraLarge))
            }

            // Conteúdo do passo atual (com animação)
            AnimatedContent(
                targetState = state.currentStepIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Próximo passo (desliza da direita)
                        (slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetX = { it }
                        ) + fadeIn()).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { -it }
                            ) + fadeOut()
                        )
                    } else {
                        // Passo anterior (desliza da esquerda)
                        (slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetX = { -it }
                        ) + fadeIn()).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { it }
                            ) + fadeOut()
                        )
                    }
                },
                label = "tutorial_step_animation",
                modifier = Modifier.weight(1f)
            ) { stepIndex ->
                state.steps.getOrNull(stepIndex)?.let { step ->
                    TutorialStepContent(step = step)
                }
            }

            // Bottom navigation
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicadores de passo (dots)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.steps.forEachIndexed { index, _ ->
                        StepIndicator(
                            isActive = index == state.currentStepIndex,
                            isCompleted = index < state.currentStepIndex
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.large))

                // Botões de navegação
                if (state.isLoading) {
                    LoadingIndicator()
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão Voltar
                        if (state.currentStepIndex > 0) {
                            IconButton(
                                onClick = { viewModel.onEvent(TutorialEvent.PreviousStep) }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Voltar"
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }

                        // Botão Próximo/Finalizar
                        PrimaryButton(
                            text = if (state.isLastStep) "Começar!" else "Próximo",
                            onClick = {
                                if (state.isLastStep) {
                                    viewModel.onEvent(TutorialEvent.Finish)
                                } else {
                                    viewModel.onEvent(TutorialEvent.NextStep)
                                }
                            },
                            icon = if (state.isLastStep) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * TutorialStepContent
 * Conteúdo de um passo do tutorial
 */
@Composable
private fun TutorialStepContent(step: OnboardingStep) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Adicionar ilustração aqui quando tivermos recursos gráficos
        // Image(painter = painterResource(step.imageResource), ...)

        // Placeholder para ilustração (emoji temporário)
        Text(
            text = when (step.id) {
                0 -> "👋"
                1 -> "🎯"
                2 -> "🎮"
                3 -> "📊"
                else -> "✨"
            },
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        // Título
        Text(
            text = step.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Descrição
        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}

/**
 * StepIndicator
 * Indicador visual de passo (dot)
 */
@Composable
private fun StepIndicator(
    isActive: Boolean,
    isCompleted: Boolean
) {
    Box(
        modifier = Modifier
            .size(if (isActive) 12.dp else 8.dp)
            .clip(CircleShape)
            .background(
                when {
                    isActive -> MaterialTheme.colorScheme.primary
                    isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
    )
}
