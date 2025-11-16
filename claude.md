# DevLanguages - Documentação do Projeto

## 1. Resumo
**Projeto:** DevLanguages
**Tipo:** Kotlin Multiplatform (KMP) + Compose Multiplatform
**Plataformas:** Android e iOS
**Package:** `com.dev.marcelo.devlanguages`

O aplicativo é uma plataforma educacional focado do desenvolvimento de habilidades em linguas de forma gamificada, como o duolingo.
A ideia em resumo é, vamos ter um aplicativo em que a pessoa escreve o prompt do que ela precisa. Exemplo: "Quero aprender ingles" ou "Quero aprender frances". A partir disso, o app vai gerar uma série de atividades gamificadas para a pessoa ir evoluindo no aprendizado da lingua escolhida.
O app existe de forma online, mas ele pode ser usado de duas formas, online jogando contra outro usuário referente a um tema especifico ou offline difitando o pronpt da lingua ou tema q ele quer estudar e a ia vai trazer quais sao os os jogos, formas dos jogos, etc...

Existem algumas features que podem ser interessantes, como por exemplo:
- Sistema de pontos e recompensas
- Níveis de dificuldade
- Feedback instantâneo
- Modo offline para praticar sem conexão, para isso teria que ter alguns temas pre-setados de algumas lingugas mais comuns.
- Notificações para lembrar o usuário de praticar diariamente
- Estatísticas de progresso e áreas de melhoria
- Personalização do plano de estudos com base no desempenho do usuário
- Desafios diários e semanais para manter o engajamento
- Suporte a múltiplas línguas para aprendizado
- Integração com assistentes virtuais para prática de conversação
- Recursos de acessibilidade para usuários com necessidades especiais
- Gamificação com badges e conquistas
- Integração com redes sociais para compartilhar progresso
- Suporte a diferentes estilos de aprendizagem (visual, auditivo, cinestésico)
- Feedback de áudio para pronúncia correta
- Modo de revisão para reforçar o aprendizado anterior
- Suporte a múltiplos dispositivos com sincronização na nuvem
- Recursos de inteligência artificial para personalizar o aprendizado
- Recursos de mindfulness para melhorar a concentração durante o estudo
- Integração com plataformas de e-learning existentes
- Suporte a diferentes métodos de pagamento para recursos premium
- Recursos de gamificação avançada, como torneios e competições entre usuários
- Suporte a diferentes níveis de proficiência, desde iniciante até avançado
- Recursos de análise de dados para identificar padrões de aprendizado
- Integração com dispositivos vestíveis para monitorar o desempenho durante o estudo
- Suporte a diferentes idiomas de interface para usuários de diversas regiões, o padrao inicial vai ser portugues br e ingles
- Recursos de segurança avançada para proteger os dados dos usuários
- Suporte a diferentes formatos de avaliação, como quizzes e testes práticos
- Recursos de personalização avançada, como temas e layouts customizáveis
- Suporte a diferentes tipos de conteúdo, como literatura, negócios e viagens

Essas ideias são brandstorms iniciais e podem ser ajustadas conforme o desenvolvimento do projeto e o feedback dos usuários. 


## 2. Objetivo do Projeto
O objetivo do projeto é para usuário que querem aprender uma lingua nova e a IA pode ajudar nesse processo. Emxemplo, estou estudando ingles e tenho dificuldade com a parte de passado e futurado, eu poderia colocar um prompt para me dar jogos sobre esse tema.
Eu quero que use alguma LLM ja existente, como a openAI, cohere, etc... para gerar os jogos e atividades gamificadas. A ideia é que o app seja freemium, então teremos que gerenciar assinaturas do usuario, esse valor vai ajudar a manter a IA rodando e trazer retorno financeiro. 
Devido isso, teremos que salvar alguns dados do usuario, como email, nome, plano de assinatura, progresso nos jogos, etc... Entao temos que ter cuidado com a parte de segurança e privacidade dos dados.
Vamos usar o firebase para isso, para todo o app, sem pensar em criar um back-end por fora. Para a ia, iremos usar o recurso chamado TOON (Token Oriented objetct notation), pois isso faz o consumo de tokens da IA seja menor.


## 3. Stack Tecnológica
- **Kotlin:** 2.2.20
- **Compose Multiplatform:** 1.9.1
- **Android Min/Target SDK:** 24 / 36

### Backend & Serviços
- **Firebase:**
  - Firebase Authentication (autenticação de usuários)
  - Firestore (banco de dados NoSQL para dados do usuário, progresso, assinaturas)
  - Outros serviços Firebase conforme necessário ao longo do projeto

### LLM & APIs

**Estratégia de LLM:**
- **Inicial (Free Tier):** **Gemini (Google)**
  - API REST simples via Ktor
  - Tier gratuito: 60 requests/minuto
  - Boa performance para conteúdo educacional
  - API Key: via Google AI Studio (https://ai.google.dev)

- **Futuro (Pago):** OpenAI GPT-4o ou Claude Sonnet
  - Migração quando escalar ou precisar de melhor qualidade
  - Trocar provider mudando apenas configuração DI

**Arquitetura Abstrata (Importante!):**
```kotlin
// Interface para abstrair o provider de LLM
interface LLMProvider {
    suspend fun generateGames(prompt: String, language: String): GamesResponse
    suspend fun generateFeedback(answer: String, correct: String): FeedbackResponse
}

// Implementações concretas
class GeminiProvider(private val apiKey: String) : LLMProvider { ... }
class OpenAIProvider(private val apiKey: String) : LLMProvider { ... }  // Futuro
class ClaudeProvider(private val apiKey: String) : LLMProvider { ... }  // Futuro

// Injeção via Koin - trocar provider mudando apenas o module
single<LLMProvider> { GeminiProvider(getProperty("GEMINI_API_KEY")) }
```

**TOON (Token Oriented Object Notation):**
- Formato de serialização para comunicação com LLM
- Reduz consumo de tokens em ~40% comparado ao JSON
- Ideal para arrays uniformes e dados estruturados
- Referência: https://github.com/toon-format/toon
- Implementar parser TOON ou usar biblioteca se existir para KMP

**Estrutura de Resposta da LLM (em TOON):**

A LLM deve retornar uma estrutura contendo:
1. **Explicação do Conteúdo** - Texto educacional sobre o tema solicitado
2. **Lista de Jogos** - Array com jogos gerados (matching, translation, fill-in-blanks)
3. **Metadados** - Língua, nível de dificuldade, tema

Exemplo conceitual:
```toon
topic: Past Tense in English
explanation: The past tense is used to describe actions that have already happened...
difficulty: intermediate
language: english

games[3]{type,question,answer,options,explanation}:
 translation,"I walked to school","Eu caminhei para a escola",null,"'Walked' is the past tense of walk"
 fill_blanks,"She ___ to the party yesterday",went,"[went,goes,go,gone]","Past tense requires 'went'"
 matching,"run|ran,eat|ate,go|went",null,null,"Match verbs with their past forms"
```

**Importante:** A estrutura final será refinada durante o desenvolvimento.

### Dependências KMP (a adicionar conforme necessário)
- [ ] Ktor Client (networking para API LLM)
- [ ] Kotlinx Serialization (JSON/TOON parsing)
- [ ] Koin (Dependency Injection)
- [ ] Voyager ou Compose Navigation (navegação entre telas)
- [ ] Firebase KMP SDK (gitlive/firebase-kotlin-sdk)
- [ ] Napier (logging multiplataforma)
- [ ] Kotlinx DateTime (manipulação de datas)
- [ ] Kamel (carregamento de imagens)
- [ ] TOON Parser (verificar se existe lib KMP ou implementar)

## 4. Arquitetura

### Padrão Arquitetural
[Clean Architecture / MVVM / MVI - Definir qual será usado]
Clean architecture com MVVM para gerenciar a separação de responsabilidades e facilitar a manutenção do código. Precisa separar cada funcionalidade em domain, data, ui. Cada tela tera esse formato. As telas serao salvas na pasta features. Separe as responsabilidades de acordo com o clean architecture.


### Estrutura de Pastas
```
commonMain/kotlin/com/dev/marcelo/devlanguages/
├── core/
│   ├── network/           # Configuração Ktor, API clients
│   ├── auth/              # Firebase Auth wrapper
│   ├── di/                # Koin modules
│   ├── database/          # Cache local (se necessário)
│   ├── utils/             # Extensions, helpers
│   ├── theme/             # Design system, cores, typography
│   └── navigation/        # Setup de navegação
├── features/              # Cada feature segue Clean Architecture
│   └── [feature-name]/
│       ├── data/          # Repository implementations, DTOs, DataSources
│       ├── domain/        # Use Cases, Models, Repository interfaces
│       └── ui/            # Composables, ViewModels, States, Events
└── [outras pastas conforme necessário]
```

**Importante:** Cada feature deve seguir a estrutura `data/domain/ui` para manter a separação de responsabilidades do Clean Architecture.

## 5. Features do App

### MVP (Minimum Viable Product) - v1.0

**Features Essenciais:**
- [ ] **Autenticação**
  - Login/Cadastro com Firebase Auth
  - Métodos: Email/Senha, Google Sign-In, Apple Sign-In (iOS), Modo Anônimo

- [ ] **Onboarding**
  - Primeira vez: escolher língua de interesse principal
  - Tutorial básico de como usar o app

- [ ] **Home/Dashboard**
  - Input de prompt (ex: "Quero aprender inglês - past tense")
  - Histórico de sessões de jogos anteriores
  - Acesso rápido a jogos recentes

- [ ] **Geração de Jogos via LLM**
  - Enviar prompt → LLM retorna jogos personalizados
  - **Campo de Explicação:** Conteúdo educacional sobre o tema antes dos jogos
    - Ex: Se prompt é "past tense", mostrar explicação sobre passado em inglês
    - Gerado pela LLM junto com os jogos

- [ ] **3 Tipos de Jogos (MVP):**
  1. **Matching** - Associar palavras/frases (ex: palavra ↔ tradução)
  2. **Translation** - Traduzir frase/palavra
  3. **Fill in the Blanks** - Completar lacunas em frases

- [ ] **Sistema de Pontuação**
  - Pontos por acerto
  - Feedback visual imediato (correto/incorreto)
  - Explicação quando errar (por que está errado)

- [ ] **Perfil do Usuário**
  - Nome, email, foto (opcional)
  - Estatísticas básicas (jogos jogados, acertos, línguas estudadas)
  - Progresso por língua/tema

- [ ] **Sistema Freemium**
  - Plano Free: 5 gerações de jogos por dia
  - Plano Premium: Ilimitado + features extras
  - Gerenciamento de assinatura (Google Play / App Store)

### Features Futuras (pós-MVP)
- [ ] Modo Multiplayer (online vs outro usuário)
- [ ] Modo Offline (jogos/temas pré-carregados)
- [ ] Áudio/Pronúncia (TTS/STT)
- [ ] Badges e conquistas
- [ ] Desafios diários/semanais
- [ ] Ranking/Leaderboard
- [ ] Notificações push (lembretes diários)
- [ ] Mais tipos de jogos (Multiple Choice, True/False, Listening, Speaking)
- [ ] Análise de progresso avançada
- [ ] Compartilhamento em redes sociais

## 6. Convenções e Regras

### Naming Conventions
- **Composables de tela:** `[Nome]Screen` (ex: `HomeScreen`)
- **ViewModels:** `[Nome]ViewModel` (ex: `HomeViewModel`)
- **States:** `[Nome]State` (ex: `HomeState`)
- **Events:** `[Nome]Event` (ex: `HomeEvent`) - usar sealed class
- **Use Cases:** `[Verbo][Nome]UseCase` (ex: `GetUserProgressUseCase`)
- **Repositories:** `[Nome]Repository` (ex: `GameRepository`)
- **Data Sources:** `[Nome]DataSource` (ex: `RemoteGameDataSource`)

### Regras de Desenvolvimento

**🚨 REGRAS CRÍTICAS:**

1. **SEMPRE pergunte ao usuário antes de executar ações importantes:**
   - **PODE criar livremente:** Pastas, arquivos de código, implementações
   - **DEVE perguntar antes:** Mudanças na arquitetura, adicionar/remover dependências grandes, mudanças no fluxo do app
   - Apresente o plano do que vai fazer antes de começar, mas não precisa pedir permissão para cada arquivo
   - Use bom senso: se é parte da implementação normal, pode criar; se é decisão de arquitetura, pergunte

2. **SEMPRE faça commit após completar cada item do MVP_CHECKLIST.md:**
   - A cada checkbox ✅ marcado no checklist, fazer commit no GitHub
   - Formato do commit: `feat: [área] - descrição do item completado`
   - Exemplos:
     - `feat: auth - implement email/password sign in use case`
     - `feat: home - create home screen UI`
     - `feat: games - implement matching game validation`
   - Commits devem ser atômicos (uma funcionalidade por vez)
   - Sempre referenciar qual item do checklist foi completado

**Outras Regras:**
1. **Clean Architecture obrigatória:** Toda feature deve ter separação clara entre `data/domain/ui`
2. **MVVM pattern:** ViewModels gerenciam estado, Composables são stateless quando possível
3. **Imutabilidade:** Sempre use `val`, `data classes` e método `copy()` para modificações
4. **Testes:** Escrever testes unitários para Use Cases e ViewModels
5. **Segurança:** Nunca commitar chaves de API, tokens ou dados sensíveis
6. **Firebase:** Validar permissões e regras de segurança do Firestore
7. **TOON:** Sempre usar TOON para comunicação com LLM para economizar tokens
8. **Idioma:** Código e variáveis em inglês, comentários podem ser em português
9. **Documentação:** Documentar funções públicas e lógica complexa
10. **Performance:** Evitar recomposições desnecessárias no Compose


## 7. Comandos Essenciais

### Build
```bash
# Android - Debug
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug

# Android - Release
./gradlew :composeApp:assembleRelease

# iOS
open iosApp/iosApp.xcodeproj
```

### Testes
```bash
# Todos os testes
./gradlew test

# Testes Android
./gradlew :composeApp:testDebugUnitTest

# Testes iOS (Simulator)
./gradlew :composeApp:iosSimulatorArm64Test
```

### Limpeza
```bash
# Limpar build
./gradlew clean

# Limpar cache completo
./gradlew clean
rm -rf build .gradle .kotlin
```

## 8. Notas Importantes

### Design & UI/UX

**🎨 Diretrizes de Design (Responsabilidade do Claude):**
- **Estilo:** Minimalista, intuitivo, bonito e chamativo
- **Contexto:** App educacional gamificado
- **Cores:** Paleta minimalista já definida (azul primary, verde secondary, roxo accent)
- **Objetivo:** Design deve facilitar o aprendizado e engajar o usuário
- **Princípios:**
  - Interfaces limpas e sem poluição visual
  - Hierarquia visual clara
  - Feedback visual imediato nas interações
  - Animações suaves e não intrusivas
  - Acessibilidade (contraste, tamanhos de fonte, áreas de toque)
  - Gamificação integrada de forma natural (não exagerada)

**Decisões de design ficam a cargo do Claude**, sempre priorizando:
1. Experiência do usuário (UX)
2. Facilidade de uso
3. Estética minimalista e moderna
4. Coerência com o design system estabelecido

### Segurança & Privacidade
- **Dados do usuário:** Email, nome, plano de assinatura, progresso nos jogos
- **LGPD/GDPR:** Cuidado com coleta e armazenamento de dados pessoais
- **Firestore Security Rules:** Validar regras de acesso aos documentos
- **Chaves de API:** Nunca commitar no repositório, usar variáveis de ambiente

### Sistema Freemium
- Modelo de negócio: Free + Premium (assinatura)
- Custo principal: Tokens da LLM
- TOON ajuda a reduzir custos em ~40%
- Gerenciamento de assinaturas via Google Play / App Store

### Limitações Técnicas
- App requer conexão para gerar novos jogos (LLM)
- Modo offline: jogos/temas pré-carregados (a definir)
- Suporte inicial: Português BR e Inglês
- Plataformas: Android (min SDK 24) e iOS

## 9. Modelos de Dados (Firestore)

### Collections Principais

**users/** - Dados do usuário
```kotlin
data class User(
    val id: String,              // Firebase Auth UID
    val email: String,
    val displayName: String,
    val photoUrl: String?,
    val createdAt: Timestamp,
    val subscription: SubscriptionPlan,  // FREE, PREMIUM
    val dailyUsageCount: Int = 0,        // Reset diário (free tier)
    val lastResetDate: String,           // Data do último reset
    val preferredLanguage: String        // Língua de interesse principal
)
```

**game_sessions/** - Histórico de sessões de jogos
```kotlin
data class GameSession(
    val id: String,
    val userId: String,
    val topic: String,                   // Ex: "Past Tense in English"
    val language: String,                // Ex: "english"
    val explanation: String,             // Explicação gerada pela LLM
    val games: List<Game>,               // Lista de jogos da sessão
    val totalScore: Int,
    val createdAt: Timestamp,
    val completedAt: Timestamp?
)
```

**user_progress/** - Progresso por língua/tema
```kotlin
data class UserProgress(
    val userId: String,
    val language: String,
    val topic: String,
    val gamesPlayed: Int,
    val correctAnswers: Int,
    val totalAnswers: Int,
    val lastPlayedAt: Timestamp
)
```

**subscriptions/** - Controle de assinaturas
```kotlin
data class Subscription(
    val userId: String,
    val plan: SubscriptionPlan,          // FREE, PREMIUM
    val startDate: Timestamp,
    val expiryDate: Timestamp?,
    val platform: String,                // "google_play", "app_store"
    val transactionId: String?
)
```

### Regras de Segurança Firestore (a implementar)
```javascript
// Usuários só podem ler/escrever seus próprios dados
match /users/{userId} {
  allow read, write: if request.auth.uid == userId;
}

match /game_sessions/{sessionId} {
  allow read, write: if request.auth.uid == resource.data.userId;
}

match /user_progress/{progressId} {
  allow read, write: if request.auth.uid == resource.data.userId;
}
```

---
**Última atualização:** 2025-11-16
