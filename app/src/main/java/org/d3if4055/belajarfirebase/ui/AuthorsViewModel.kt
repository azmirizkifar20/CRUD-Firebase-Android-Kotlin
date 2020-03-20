package org.d3if4055.belajarfirebase.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import org.d3if4055.belajarfirebase.data.Author
import org.d3if4055.belajarfirebase.data.NODE_AUTHORS

class AuthorsViewModel : ViewModel() {
    // make database
    private val dbAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)

    // create list of author
    private val _authors = MutableLiveData<List<Author>>()
    val authors: LiveData<List<Author>>
        get() = _authors

    // create single author
    private val _author = MutableLiveData<Author>()
    val author: LiveData<Author>
        get() = _author

    // create result
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    // fungsi untuk menambahkan data ke firebase realtime database
    fun addAuthor(author: Author) {
        author.id = dbAuthors.push().key
        dbAuthors.child(author.id!!).setValue(author).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    // buat event untuk perubahan data untuk realtime update
    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) { }

        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) { }

        // update data otomatis ketika data di edit
        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        // update data otomatis ketika data ditambahkan
        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            author?.isDeleted = true
            _author.value = author
        }
    }

    // buat fungsi get realtimeupdate
    fun getRealtimeUpdates() {
        dbAuthors.addChildEventListener(childEventListener)
    }

    // buat event untuk menampilkan data di firebase dengan metode fetching
    private val valueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) { }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val authors = mutableListOf<Author>()
                for (authorSnapshot in snapshot.children) {
                    val author = authorSnapshot.getValue(Author::class.java)
                    author?.id = authorSnapshot.key
                    author?.let { authors.add(it) }
                }
                _authors.value = authors
            }
        }
    }

    // fetch author untuk menampilkan data di firebase
    fun fetchAuthors() {
        dbAuthors.addListenerForSingleValueEvent(valueEventListener)
    }

    // fungsi update
    fun updateAuthor(author: Author) {
        dbAuthors.child(author.id!!).setValue(author).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    fun deleteAuthor(author: Author) {
        dbAuthors.child(author.id!!).setValue(null).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dbAuthors.removeEventListener(childEventListener)
    }
}