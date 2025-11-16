# DevLanguages - MVP Checklist

> **Regra:** A cada item ✅ completado, fazer commit no GitHub
> **Formato do commit:** `feat: [área] - descrição do item`

---

## 1. Setup Inicial do Projeto

### 1.1 Configuração Base
- [ ] Adicionar dependências KMP no `gradle/libs.versions.toml`
  - Ktor Client
  - Kotlinx Serialization
  - Koin
  - Voyager (navegação)
  - Firebase KMP SDK
  - Napier (logging)
  - Kotlinx DateTime
  - Kamel (imagens)
- [ ] Configurar plugins no `build.gradle.kts`
- [ ] Criar arquivo de configuração para chaves de API (local.properties)
- [ ] Adicionar .gitignore para chaves sensíveis

### 1.2 Estrutura de Pastas
- [ ] Criar pasta `core/` e subpastas
  - `core/network/`
  - `core/auth/`
  - `core/di/`
  - `core/utils/`
  - `core/theme/`
  - `core/navigation/`
- [ ] Criar pasta `features/`

---

## 2. Core Infrastructure

### 2.1 Theme e Design System
- [ ] Criar `core/theme/Color.kt` - definir paleta de cores
- [ ] Criar `core/theme/Typography.kt` - definir tipografia
- [ ] Criar `core/theme/Theme.kt` - tema principal
- [ ] Criar `core/theme/Spacing.kt` - espaçamentos padronizados
- [ ] Criar componentes reutilizáveis básicos
  - `PrimaryButton.kt`
  - `SecondaryButton.kt`
  - `CustomTextField.kt`
  - `LoadingIndicator.kt`

### 2.2 Navegação
- [ ] Criar `core/navigation/Screen.kt` (sealed class com todas as telas)
- [ ] Configurar Voyager Navigator
- [ ] Criar `core/navigation/NavigationExtensions.kt` (funções helper)

### 2.3 Network Layer
- [ ] Criar `core/network/KtorClient.kt` - configuração do Ktor
- [ ] Criar `core/network/ApiResult.kt` - sealed class para resultados
- [ ] Criar `core/network/NetworkException.kt` - tratamento de erros

### 2.4 LLM Provider (Abstração)
- [ ] Criar interface `core/network/llm/LLMProvider.kt`
- [ ] Criar `core/network/llm/GeminiProvider.kt` (implementação)
- [ ] Criar models de request/response
  - `GameGenerationRequest.kt`
  - `GameGenerationResponse.kt`
- [ ] Implementar TOON parser básico
  - `core/network/toon/ToonParser.kt`
  - Parsear estrutura: topic, explanation, games[]

### 2.5 Firebase Setup
- [ ] Configurar Firebase no projeto Android (`google-services.json`)
- [ ] Configurar Firebase no projeto iOS (`GoogleService-Info.plist`)
- [ ] Criar `core/auth/FirebaseAuthWrapper.kt` (abstração multiplataforma)
- [ ] Criar `core/database/FirestoreWrapper.kt` (abstração multiplataforma)

### 2.6 Dependency Injection (Koin)
- [ ] Criar `core/di/NetworkModule.kt`
- [ ] Criar `core/di/FirebaseModule.kt`
- [ ] Criar `core/di/AppModule.kt`
- [ ] Configurar Koin na inicialização do app

### 2.7 Utils
- [ ] Criar `core/utils/DateTimeUtils.kt`
- [ ] Criar `core/utils/StringExtensions.kt`
- [ ] Criar `core/utils/ValidationUtils.kt` (email, senha, etc)

---

## 3. Feature: Autenticação

### 3.1 Domain Layer
- [ ] Criar `features/auth/domain/model/User.kt`
- [ ] Criar `features/auth/domain/model/AuthResult.kt` (sealed class)
- [ ] Criar `features/auth/domain/repository/AuthRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/auth/domain/usecase/SignInWithEmailUseCase.kt`
  - `features/auth/domain/usecase/SignUpWithEmailUseCase.kt`
  - `features/auth/domain/usecase/SignInWithGoogleUseCase.kt`
  - `features/auth/domain/usecase/SignInWithAppleUseCase.kt`
  - `features/auth/domain/usecase/SignInAnonymouslyUseCase.kt`
  - `features/auth/domain/usecase/SignOutUseCase.kt`
  - `features/auth/domain/usecase/GetCurrentUserUseCase.kt`

### 3.2 Data Layer
- [ ] Criar `features/auth/data/datasource/AuthDataSource.kt` (interface)
- [ ] Criar `features/auth/data/datasource/FirebaseAuthDataSource.kt` (implementação)
- [ ] Criar `features/auth/data/repository/AuthRepositoryImpl.kt`
- [ ] Criar DTOs se necessário
- [ ] Configurar Koin module: `features/auth/data/di/AuthModule.kt`

### 3.3 UI Layer
- [ ] Criar `features/auth/ui/login/LoginScreen.kt`
- [ ] Criar `features/auth/ui/login/LoginViewModel.kt`
- [ ] Criar `features/auth/ui/login/LoginState.kt` (data class)
- [ ] Criar `features/auth/ui/login/LoginEvent.kt` (sealed class)
- [ ] Criar `features/auth/ui/signup/SignUpScreen.kt`
- [ ] Criar `features/auth/ui/signup/SignUpViewModel.kt`
- [ ] Criar `features/auth/ui/signup/SignUpState.kt`
- [ ] Criar `features/auth/ui/signup/SignUpEvent.kt`
- [ ] Implementar validação de email/senha
- [ ] Implementar Google Sign-In (Android e iOS)
- [ ] Implementar Apple Sign-In (iOS)
- [ ] Implementar login anônimo
- [ ] Adicionar loading states e error handling

### 3.4 Testes
- [ ] Testes unitários: `SignInWithEmailUseCaseTest.kt`
- [ ] Testes unitários: `SignUpWithEmailUseCaseTest.kt`
- [ ] Testes unitários: `LoginViewModelTest.kt`
- [ ] Testes unitários: `SignUpViewModelTest.kt`

---

## 4. Feature: Onboarding

### 4.1 Domain Layer
- [ ] Criar `features/onboarding/domain/model/OnboardingStep.kt`
- [ ] Criar `features/onboarding/domain/model/Language.kt` (enum ou data class)
- [ ] Criar `features/onboarding/domain/repository/OnboardingRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/onboarding/domain/usecase/SavePreferredLanguageUseCase.kt`
  - `features/onboarding/domain/usecase/CompleteOnboardingUseCase.kt`

### 4.2 Data Layer
- [ ] Criar `features/onboarding/data/datasource/OnboardingDataSource.kt`
- [ ] Criar `features/onboarding/data/repository/OnboardingRepositoryImpl.kt`
- [ ] Salvar preferência de língua no Firestore
- [ ] Configurar Koin module

### 4.3 UI Layer
- [ ] Criar `features/onboarding/ui/OnboardingScreen.kt` (tela com steps)
- [ ] Criar `features/onboarding/ui/OnboardingViewModel.kt`
- [ ] Criar `features/onboarding/ui/OnboardingState.kt`
- [ ] Criar `features/onboarding/ui/OnboardingEvent.kt`
- [ ] Implementar seleção de língua de interesse
- [ ] Implementar tutorial básico (3-4 slides)
- [ ] Implementar navegação entre steps
- [ ] Adicionar animações de transição

### 4.4 Testes
- [ ] Testes unitários: `SavePreferredLanguageUseCaseTest.kt`
- [ ] Testes unitários: `OnboardingViewModelTest.kt`

---

## 5. Feature: Home/Dashboard

### 5.1 Domain Layer
- [ ] Criar `features/home/domain/model/GameSession.kt`
- [ ] Criar `features/home/domain/repository/HomeRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/home/domain/usecase/GetUserSessionsUseCase.kt`
  - `features/home/domain/usecase/GetUserStatsUseCase.kt`
  - `features/home/domain/usecase/CheckDailyLimitUseCase.kt` (freemium)

### 5.2 Data Layer
- [ ] Criar `features/home/data/datasource/HomeDataSource.kt`
- [ ] Criar `features/home/data/repository/HomeRepositoryImpl.kt`
- [ ] Buscar sessões de jogos do Firestore
- [ ] Buscar estatísticas do usuário
- [ ] Configurar Koin module

### 5.3 UI Layer
- [ ] Criar `features/home/ui/HomeScreen.kt`
- [ ] Criar `features/home/ui/HomeViewModel.kt`
- [ ] Criar `features/home/ui/HomeState.kt`
- [ ] Criar `features/home/ui/HomeEvent.kt`
- [ ] Implementar input de prompt (TextField grande)
- [ ] Implementar botão "Gerar Jogos"
- [ ] Implementar lista de sessões anteriores (histórico)
- [ ] Implementar card de estatísticas (jogos jogados, acertos, etc)
- [ ] Implementar indicador de limite diário (free tier)
- [ ] Adicionar floating action button para criar nova sessão
- [ ] Implementar pull-to-refresh

### 5.4 Testes
- [ ] Testes unitários: `GetUserSessionsUseCaseTest.kt`
- [ ] Testes unitários: `CheckDailyLimitUseCaseTest.kt`
- [ ] Testes unitários: `HomeViewModelTest.kt`

---

## 6. Feature: Geração de Jogos via LLM

### 6.1 Domain Layer
- [ ] Criar `features/gamegen/domain/model/GameGenerationRequest.kt`
- [ ] Criar `features/gamegen/domain/model/GeneratedContent.kt` (explanation + games)
- [ ] Criar `features/gamegen/domain/model/Game.kt` (base class)
- [ ] Criar `features/gamegen/domain/model/GameType.kt` (enum: MATCHING, TRANSLATION, FILL_BLANKS)
- [ ] Criar models específicos:
  - `features/gamegen/domain/model/MatchingGame.kt`
  - `features/gamegen/domain/model/TranslationGame.kt`
  - `features/gamegen/domain/model/FillBlanksGame.kt`
- [ ] Criar `features/gamegen/domain/repository/GameGenerationRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/gamegen/domain/usecase/GenerateGamesUseCase.kt`
  - `features/gamegen/domain/usecase/SaveGameSessionUseCase.kt`

### 6.2 Data Layer
- [ ] Criar `features/gamegen/data/datasource/LLMDataSource.kt`
- [ ] Criar `features/gamegen/data/datasource/LLMDataSourceImpl.kt`
- [ ] Criar `features/gamegen/data/dto/GameGenerationDTO.kt`
- [ ] Implementar parser TOON → Models de domínio
- [ ] Criar `features/gamegen/data/repository/GameGenerationRepositoryImpl.kt`
- [ ] Salvar sessão gerada no Firestore
- [ ] Incrementar contador de uso diário (freemium)
- [ ] Configurar Koin module

### 6.3 UI Layer
- [ ] Criar `features/gamegen/ui/GeneratingScreen.kt` (loading enquanto gera)
- [ ] Criar `features/gamegen/ui/GeneratingViewModel.kt`
- [ ] Criar `features/gamegen/ui/GeneratingState.kt`
- [ ] Criar `features/gamegen/ui/GeneratingEvent.kt`
- [ ] Criar `features/gamegen/ui/ExplanationScreen.kt` (mostra explicação antes dos jogos)
- [ ] Criar `features/gamegen/ui/ExplanationViewModel.kt`
- [ ] Implementar loading state (animação enquanto LLM processa)
- [ ] Implementar error handling (falha na API, limite excedido)
- [ ] Implementar navegação: Home → Generating → Explanation → Games

### 6.4 Testes
- [ ] Testes unitários: `GenerateGamesUseCaseTest.kt`
- [ ] Testes unitários: `SaveGameSessionUseCaseTest.kt`
- [ ] Testes unitários: `GeneratingViewModelTest.kt`
- [ ] Testes de integração: LLM API + TOON parser

---

## 7. Feature: Game Engine (Jogos)

### 7.1 Domain Layer
- [ ] Criar `features/games/domain/model/GameAnswer.kt`
- [ ] Criar `features/games/domain/model/GameResult.kt`
- [ ] Criar `features/games/domain/repository/GameRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/games/domain/usecase/ValidateAnswerUseCase.kt`
  - `features/games/domain/usecase/CalculateScoreUseCase.kt`
  - `features/games/domain/usecase/SaveProgressUseCase.kt`

### 7.2 Data Layer
- [ ] Criar `features/games/data/datasource/GameDataSource.kt`
- [ ] Criar `features/games/data/repository/GameRepositoryImpl.kt`
- [ ] Salvar progresso no Firestore (user_progress collection)
- [ ] Configurar Koin module

### 7.3 UI Layer - Matching Game
- [ ] Criar `features/games/ui/matching/MatchingGameScreen.kt`
- [ ] Criar `features/games/ui/matching/MatchingGameViewModel.kt`
- [ ] Criar `features/games/ui/matching/MatchingGameState.kt`
- [ ] Criar `features/games/ui/matching/MatchingGameEvent.kt`
- [ ] Implementar drag & drop ou clique para associar
- [ ] Implementar validação de resposta
- [ ] Implementar feedback visual (correto/incorreto)
- [ ] Implementar animações

### 7.4 UI Layer - Translation Game
- [ ] Criar `features/games/ui/translation/TranslationGameScreen.kt`
- [ ] Criar `features/games/ui/translation/TranslationGameViewModel.kt`
- [ ] Criar `features/games/ui/translation/TranslationGameState.kt`
- [ ] Criar `features/games/ui/translation/TranslationGameEvent.kt`
- [ ] Implementar TextField para resposta
- [ ] Implementar validação (pode ter múltiplas respostas corretas)
- [ ] Implementar feedback com explicação
- [ ] Implementar botão "Verificar" e "Próximo"

### 7.5 UI Layer - Fill in the Blanks Game
- [ ] Criar `features/games/ui/fillblanks/FillBlanksGameScreen.kt`
- [ ] Criar `features/games/ui/fillblanks/FillBlanksGameViewModel.kt`
- [ ] Criar `features/games/ui/fillblanks/FillBlanksGameState.kt`
- [ ] Criar `features/games/ui/fillblanks/FillBlanksGameEvent.kt`
- [ ] Implementar frase com lacuna destacada
- [ ] Implementar opções de resposta (4 opções)
- [ ] Implementar validação
- [ ] Implementar feedback com explicação

### 7.6 UI Layer - Game Container
- [ ] Criar `features/games/ui/GameContainerScreen.kt` (gerencia fluxo entre jogos)
- [ ] Criar `features/games/ui/GameContainerViewModel.kt`
- [ ] Criar `features/games/ui/GameContainerState.kt`
- [ ] Implementar navegação entre jogos da sessão
- [ ] Implementar progress indicator (ex: 1/10 jogos)
- [ ] Implementar botão "Pular" (opcional)
- [ ] Implementar tela de resumo final (score total, acertos/erros)

### 7.7 Testes
- [ ] Testes unitários: `ValidateAnswerUseCaseTest.kt`
- [ ] Testes unitários: `CalculateScoreUseCaseTest.kt`
- [ ] Testes unitários: `MatchingGameViewModelTest.kt`
- [ ] Testes unitários: `TranslationGameViewModelTest.kt`
- [ ] Testes unitários: `FillBlanksGameViewModelTest.kt`

---

## 8. Feature: Sistema de Pontuação e Feedback

### 8.1 Domain Layer
- [ ] Criar `features/score/domain/model/Score.kt`
- [ ] Criar `features/score/domain/model/Feedback.kt`
- [ ] Criar regras de pontuação (ex: 10 pontos por acerto)
- [ ] Criar Use Cases:
  - `features/score/domain/usecase/CalculateFinalScoreUseCase.kt`
  - `features/score/domain/usecase/GenerateFeedbackUseCase.kt`

### 8.2 Data Layer
- [ ] Criar `features/score/data/repository/ScoreRepositoryImpl.kt`
- [ ] Salvar score no Firestore (game_sessions)
- [ ] Atualizar user_progress
- [ ] Configurar Koin module

### 8.3 UI Layer
- [ ] Criar `features/score/ui/ScoreScreen.kt` (tela final com resultados)
- [ ] Criar `features/score/ui/ScoreViewModel.kt`
- [ ] Criar `features/score/ui/ScoreState.kt`
- [ ] Implementar visualização de score total
- [ ] Implementar lista de acertos/erros
- [ ] Implementar botão "Jogar Novamente"
- [ ] Implementar botão "Voltar ao Início"
- [ ] Adicionar animações de comemoração (confete, etc)

### 8.4 Testes
- [ ] Testes unitários: `CalculateFinalScoreUseCaseTest.kt`
- [ ] Testes unitários: `ScoreViewModelTest.kt`

---

## 9. Feature: Perfil do Usuário

### 9.1 Domain Layer
- [ ] Criar `features/profile/domain/model/UserProfile.kt`
- [ ] Criar `features/profile/domain/model/UserStats.kt`
- [ ] Criar `features/profile/domain/repository/ProfileRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/profile/domain/usecase/GetUserProfileUseCase.kt`
  - `features/profile/domain/usecase/UpdateUserProfileUseCase.kt`
  - `features/profile/domain/usecase/GetUserStatsUseCase.kt`

### 9.2 Data Layer
- [ ] Criar `features/profile/data/datasource/ProfileDataSource.kt`
- [ ] Criar `features/profile/data/repository/ProfileRepositoryImpl.kt`
- [ ] Buscar dados do Firestore (users, game_sessions, user_progress)
- [ ] Atualizar dados do usuário
- [ ] Configurar Koin module

### 9.3 UI Layer
- [ ] Criar `features/profile/ui/ProfileScreen.kt`
- [ ] Criar `features/profile/ui/ProfileViewModel.kt`
- [ ] Criar `features/profile/ui/ProfileState.kt`
- [ ] Criar `features/profile/ui/ProfileEvent.kt`
- [ ] Implementar exibição de foto, nome, email
- [ ] Implementar edição de nome
- [ ] Implementar upload de foto (opcional para MVP)
- [ ] Implementar estatísticas:
  - Total de jogos jogados
  - Taxa de acerto
  - Línguas estudadas
  - Temas estudados
  - Progresso por língua
- [ ] Implementar botão de logout
- [ ] Implementar navegação para configurações

### 9.4 Testes
- [ ] Testes unitários: `GetUserProfileUseCaseTest.kt`
- [ ] Testes unitários: `GetUserStatsUseCaseTest.kt`
- [ ] Testes unitários: `ProfileViewModelTest.kt`

---

## 10. Feature: Sistema Freemium

### 10.1 Domain Layer
- [ ] Criar `features/subscription/domain/model/SubscriptionPlan.kt` (enum: FREE, PREMIUM)
- [ ] Criar `features/subscription/domain/model/Subscription.kt`
- [ ] Criar `features/subscription/domain/model/UsageLimit.kt`
- [ ] Criar `features/subscription/domain/repository/SubscriptionRepository.kt` (interface)
- [ ] Criar Use Cases:
  - `features/subscription/domain/usecase/GetCurrentSubscriptionUseCase.kt`
  - `features/subscription/domain/usecase/CheckUsageLimitUseCase.kt`
  - `features/subscription/domain/usecase/IncrementDailyUsageUseCase.kt`
  - `features/subscription/domain/usecase/ResetDailyUsageUseCase.kt`
  - `features/subscription/domain/usecase/UpgradeToPremiumUseCase.kt`

### 10.2 Data Layer
- [ ] Criar `features/subscription/data/datasource/SubscriptionDataSource.kt`
- [ ] Criar `features/subscription/data/repository/SubscriptionRepositoryImpl.kt`
- [ ] Implementar lógica de reset diário (verificar data)
- [ ] Salvar/atualizar subscription no Firestore
- [ ] Configurar Koin module

### 10.3 UI Layer
- [ ] Criar `features/subscription/ui/PaywallScreen.kt` (quando atingir limite)
- [ ] Criar `features/subscription/ui/PaywallViewModel.kt`
- [ ] Criar `features/subscription/ui/PaywallState.kt`
- [ ] Criar `features/subscription/ui/SubscriptionScreen.kt` (detalhes dos planos)
- [ ] Criar `features/subscription/ui/SubscriptionViewModel.kt`
- [ ] Implementar comparação de planos (Free vs Premium)
- [ ] Implementar integração com Google Play Billing (Android)
- [ ] Implementar integração com StoreKit (iOS)
- [ ] Implementar verificação de compra
- [ ] Implementar restauração de compras
- [ ] Implementar tratamento de erro em compras

### 10.4 Testes
- [ ] Testes unitários: `CheckUsageLimitUseCaseTest.kt`
- [ ] Testes unitários: `IncrementDailyUsageUseCaseTest.kt`
- [ ] Testes unitários: `ResetDailyUsageUseCaseTest.kt`
- [ ] Testes unitários: `PaywallViewModelTest.kt`

---

## 11. Integração e Polimento

### 11.1 Navegação Completa
- [ ] Implementar fluxo completo: Login → Onboarding → Home → Game Generation → Games → Score → Home
- [ ] Implementar deep linking (se aplicável)
- [ ] Implementar back navigation correta
- [ ] Testar navegação em Android
- [ ] Testar navegação em iOS

### 11.2 Error Handling Global
- [ ] Implementar tratamento de erro de rede
- [ ] Implementar tratamento de erro de autenticação
- [ ] Implementar tratamento de erro de LLM API
- [ ] Implementar retry logic
- [ ] Implementar mensagens de erro user-friendly

### 11.3 Loading States
- [ ] Adicionar loading indicators em todas as telas necessárias
- [ ] Implementar skeleton loading (opcional)
- [ ] Garantir que não há UI bloqueante

### 11.4 Localização (i18n)
- [ ] Configurar suporte a múltiplos idiomas
- [ ] Criar strings em Português BR
- [ ] Criar strings em Inglês
- [ ] Testar troca de idioma

### 11.5 Acessibilidade
- [ ] Adicionar content descriptions para leitores de tela
- [ ] Garantir contraste adequado de cores
- [ ] Testar com TalkBack (Android)
- [ ] Testar com VoiceOver (iOS)

---

## 12. Testes Finais

### 12.1 Testes Unitários
- [ ] Cobertura mínima de 70% nos Use Cases
- [ ] Cobertura mínima de 70% nos ViewModels
- [ ] Todos os testes passando

### 12.2 Testes de Integração
- [ ] Testar fluxo completo: Cadastro → Geração → Jogos → Score
- [ ] Testar integração Firebase Auth
- [ ] Testar integração Firestore
- [ ] Testar integração LLM API

### 12.3 Testes Manuais
- [ ] Testar em dispositivo Android real
- [ ] Testar em emulador Android
- [ ] Testar em dispositivo iOS real
- [ ] Testar em simulador iOS
- [ ] Testar em diferentes tamanhos de tela
- [ ] Testar modo claro/escuro
- [ ] Testar com conexão lenta
- [ ] Testar offline (deve mostrar erro apropriado)

---

## 13. Segurança e Privacidade

### 13.1 Firebase Security Rules
- [ ] Configurar regras de segurança do Firestore
- [ ] Testar regras (usuários só acessam seus dados)
- [ ] Configurar regras de Storage (se aplicável)

### 13.2 Chaves de API
- [ ] Garantir que chaves não estão commitadas
- [ ] Documentar como configurar chaves localmente
- [ ] Configurar variáveis de ambiente para CI/CD

### 13.3 Dados Sensíveis
- [ ] Validar que senhas não são logadas
- [ ] Validar que tokens não são expostos
- [ ] Implementar política de privacidade (texto básico)

---

## 14. Build e Deploy

### 14.1 Android
- [ ] Configurar assinatura de APK/AAB
- [ ] Gerar build de release
- [ ] Testar build de release
- [ ] Configurar ProGuard/R8 (obfuscação)
- [ ] Preparar assets (ícone, splash screen)
- [ ] Criar screenshots para Play Store
- [ ] Escrever descrição para Play Store

### 14.2 iOS
- [ ] Configurar certificados e provisioning profiles
- [ ] Gerar build de release
- [ ] Testar build de release
- [ ] Preparar assets (ícone, launch screen)
- [ ] Criar screenshots para App Store
- [ ] Escrever descrição para App Store

### 14.3 CI/CD (Opcional para MVP)
- [ ] Configurar GitHub Actions para testes
- [ ] Configurar build automático

---

## 15. Documentação

### 15.1 Código
- [ ] Documentar funções públicas principais
- [ ] Adicionar comentários em código complexo
- [ ] Atualizar README.md com instruções de setup

### 15.2 Usuário
- [ ] Criar termos de uso (básico)
- [ ] Criar política de privacidade (básico)
- [ ] Criar FAQ (perguntas frequentes)

---

## 16. Launch Checklist

- [ ] Todos os testes passando
- [ ] Build de release funcionando (Android + iOS)
- [ ] Firebase configurado em produção
- [ ] LLM API configurada
- [ ] Sistema de assinatura testado
- [ ] Política de privacidade publicada
- [ ] Screenshots e descrição prontos
- [ ] Submeter para Google Play Store (Android)
- [ ] Submeter para Apple App Store (iOS)

---

**Status Geral:** 0% completo (0/X tarefas)

**Última atualização:** 2025-11-16
