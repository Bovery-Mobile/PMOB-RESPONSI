package com.pmob.projectakhirpemrogramanmobile

import com.pmob.projectakhirpemrogramanmobile.network.RetrofitInstance
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val rvBooks = findViewById<RecyclerView>(R.id.rvBooks)
        rvBooks.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.searchBooks("novel indonesia")

                val books = response.items?.map {
                    Book(
                        title = it.volumeInfo.title,
                        author = it.volumeInfo.authors?.joinToString(", ") ?: "-",
                        coverUrl = it.volumeInfo.imageLinks?.thumbnail
                    )
                } ?: emptyList<Book>()


                rvBooks.adapter = BookAdapter(books)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


