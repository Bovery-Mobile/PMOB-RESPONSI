package com.pmob.projectakhirpemrogramanmobile.ui.library

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pmob.projectakhirpemrogramanmobile.Book
import com.pmob.projectakhirpemrogramanmobile.BookAdapter
import com.pmob.projectakhirpemrogramanmobile.Purchase
import com.pmob.projectakhirpemrogramanmobile.R
import com.pmob.projectakhirpemrogramanmobile.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment(R.layout.fragment_library) {

    private lateinit var binding: FragmentLibraryBinding
    private val bookList = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLibraryBinding.bind(view)

        adapter = BookAdapter(bookList) { book ->
            // Handle book click - bisa membuka DetailBookActivity jika diinginkan
            // val intent = Intent(requireContext(), DetailBookActivity::class.java)
            // intent.putExtra("BOOK_ID", book.id)
            // startActivity(intent)
        }
        binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibrary.adapter = adapter

        loadLibrary()
    }

    private fun loadLibrary() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val dbRef = FirebaseDatabase.getInstance()
            .getReference("purchases")
            .child(uid)

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()

                for (data in snapshot.children) {
                    val purchase = data.getValue(Purchase::class.java)
                    if (purchase != null && purchase.status.lowercase() == "success") {
                        // Convert purchase ke book
                        val book = Book(
                            id = purchase.id.hashCode(), // Convert String to Int
                            title = purchase.title,
                            author = "",
                            coverUrl = purchase.bookCover,
                            rating = 0.0,
                            publishedYear = 0,
                            pages = 0,
                            genres = listOf("Dibeli"),
                            synopsis = "",
                            price = purchase.price,
                            category = "Dibeli"
                        )
                        bookList.add(book)
                    }
                }

                // Update UI
                if (bookList.isEmpty()) {
                    binding.rvLibrary.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.rvLibrary.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Gagal memuat library",
                    Toast.LENGTH_SHORT
                ).show()

                Log.e("LibraryFragment", error.message)
            }

        })
    }
}
