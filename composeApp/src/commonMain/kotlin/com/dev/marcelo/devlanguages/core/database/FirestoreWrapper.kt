package com.dev.marcelo.devlanguages.core.database

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.QuerySnapshot
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow

/**
 * Firestore Wrapper
 * Wrapper para facilitar o uso do Firestore em KMP
 */
class FirestoreWrapper {
    private val firestore: FirebaseFirestore = Firebase.firestore

    /**
     * Referência a uma coleção
     */
    fun collection(path: String) = firestore.collection(path)

    /**
     * Referência a um documento
     */
    fun document(path: String): DocumentReference = firestore.document(path)

    /**
     * Salva um documento (cria ou atualiza)
     * @param collection Nome da coleção
     * @param documentId ID do documento
     * @param data Dados a serem salvos
     */
    suspend fun setDocument(
        collection: String,
        documentId: String,
        data: Any
    ) {
        firestore.collection(collection)
            .document(documentId)
            .set(data)
    }

    /**
     * Obtém um documento
     * @param collection Nome da coleção
     * @param documentId ID do documento
     * @return DocumentSnapshot ou null se não existir
     */
    suspend fun getDocument(
        collection: String,
        documentId: String
    ): DocumentSnapshot? {
        return try {
            firestore.collection(collection)
                .document(documentId)
                .get()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Obtém um documento tipado
     */
    suspend inline fun <reified T> getDocumentTyped(
        collection: String,
        documentId: String
    ): T? {
        return getDocument(collection, documentId)?.data<T>()
    }

    /**
     * Deleta um documento
     */
    suspend fun deleteDocument(
        collection: String,
        documentId: String
    ) {
        firestore.collection(collection)
            .document(documentId)
            .delete()
    }

    /**
     * Atualiza campos específicos de um documento
     */
    suspend fun updateDocument(
        collection: String,
        documentId: String,
        updates: Map<String, Any?>
    ) {
        firestore.collection(collection)
            .document(documentId)
            .update(updates)
    }

    /**
     * Query simples - obtém todos os documentos de uma coleção
     */
    suspend fun getCollection(collection: String): QuerySnapshot {
        return firestore.collection(collection).get()
    }

    /**
     * Observa um documento em tempo real
     */
    fun observeDocument(
        collection: String,
        documentId: String
    ): Flow<DocumentSnapshot?> {
        return firestore.collection(collection)
            .document(documentId)
            .snapshots
    }

    /**
     * Observa uma coleção em tempo real
     */
    fun observeCollection(collection: String): Flow<QuerySnapshot> {
        return firestore.collection(collection).snapshots
    }

    /**
     * Batch write - múltiplas operações em uma transação
     */
    suspend fun batch(block: suspend FirebaseFirestore.() -> Unit) {
        firestore.block()
    }
}

/**
 * Collection names - constantes para nomes de coleções
 */
object FirestoreCollections {
    const val USERS = "users"
    const val GAME_SESSIONS = "game_sessions"
    const val USER_PROGRESS = "user_progress"
    const val SUBSCRIPTIONS = "subscriptions"
}
