package com.tam.gojual.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tam.gojual.data.model.Post
import kotlinx.coroutines.tasks.await

class PostRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val postsCollection = firestore.collection("posts")

    /**
     * Fungsi untuk menambahkan postingan baru ke Firestore.
     */
    suspend fun addPost(post: Post): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Pengguna tidak login"))

            val finalPost = post.copy(
                sellerUid = currentUser.uid,
                sellerName = currentUser.displayName ?: "Nama Tidak Ada"
            )

            postsCollection.add(finalPost).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fungsi untuk mengambil daftar postingan dengan paginasi dan fungsionalitas pencarian.
     */
    suspend fun getPosts(
        limit: Long,
        lastVisible: DocumentSnapshot?,
        searchQuery: String? = null
    ): Pair<List<Post>, DocumentSnapshot?> {
        // Logika ini akan berjalan setiap kali ViewModel meminta data
        try {
            Log.d("PostRepository", "Mencoba mengambil data dengan query: '$searchQuery'")

            var query: Query = postsCollection.orderBy("createdAt", Query.Direction.DESCENDING)

            if (!searchQuery.isNullOrBlank()) {
                query = query.whereGreaterThanOrEqualTo("title", searchQuery)
                    .whereLessThanOrEqualTo("title", searchQuery + '\uf8ff')
            }

            query = query.limit(limit)

            val finalQuery = if (lastVisible != null) {
                query.startAfter(lastVisible)
            } else {
                query
            }

            val querySnapshot = finalQuery.get().await()

            // LOG "MATA-MATA" PENTING #1
            Log.d("PostRepository", "Query berhasil, ditemukan ${querySnapshot.size()} dokumen.")

            val posts = querySnapshot.documents.mapNotNull { doc ->
                val post = doc.toObject(Post::class.java)
                post?.id = doc.id
                post
            }
            val newLastVisible = querySnapshot.documents.lastOrNull()
            return Pair(posts, newLastVisible)

        } catch (e: Exception) {
            // LOG "MATA-MATA" PENTING #2: Jika ada error saat query ke Firestore
            Log.e("PostRepository", "Error saat menjalankan query getPosts: ", e)
            return Pair(emptyList(), null) // Kembalikan daftar kosong jika ada error
        }
    }

    /**
     * Fungsi untuk mengambil satu detail postingan berdasarkan ID-nya.
     */
    suspend fun getPostById(postId: String): Post? {
        return try {
            val doc = postsCollection.document(postId).get().await()
            val post = doc.toObject(Post::class.java)
            post?.id = doc.id
            post
        } catch (e: Exception) {
            null
        }
    }
}