package com.pmob.projectakhirpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.ChipGroup
import com.pmob.projectakhirpemrogramanmobile.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: BookRepository

    private val allBooks = mutableListOf<Book>()
    private var filteredBooks = mutableListOf<Book>()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = BookRepository()

        setupSearch()
        setupChipFilters()
        loadInitialBooks()
    }

    // ===================== LOAD DATA =====================

    private fun loadInitialBooks() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val categorizedBooks = repository.getPopularBooks()

                allBooks.clear()
                categorizedBooks.values.forEach { books ->
                    allBooks.addAll(books)
                }

                filteredBooks.clear()
                filteredBooks.addAll(allBooks)

                showLoading(false)
                displayCategories(categorizedBooks)

            } catch (e: Exception) {
                e.printStackTrace()
                showLoading(false)
                showError("Gagal memuat data. Periksa koneksi internet.")
            }
        }
    }

    // ===================== SEARCH =====================

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                val query = s.toString().trim()
                when {
                    query.length >= 3 -> searchBooks(query)
                    query.isEmpty() -> loadInitialBooks()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchBooks(query: String) {
        if (isLoading) return

        showLoading(true)

        lifecycleScope.launch {
            try {
                val books = repository.searchBooks(query)

                allBooks.clear()
                allBooks.addAll(books)

                filteredBooks = allBooks.toMutableList()
                updateDisplay()

            } catch (e: Exception) {
                e.printStackTrace()
                showLoading(false)
                showError("Gagal mencari buku")
            }
        }
    }

    // ===================== CHIP FILTER =====================

    private fun setupChipFilters() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener

            val query = binding.etSearch.text.toString().trim().lowercase()

            filteredBooks = when (checkedIds[0]) {
                R.id.chipFiction -> filterByCategory("Fiction", "Fiksi", "FICTION")
                R.id.chipClassic -> filterByCategory("Classic", "Klasik", "CLASSIC")
                else -> allBooks.toMutableList()
            }

            if (query.isNotEmpty()) {
                filteredBooks = filteredBooks.filter { book ->
                    book.title.lowercase().contains(query) ||
                            book.author.lowercase().contains(query)
                }.toMutableList()
            }

            updateDisplay()
        }
    }

    private fun filterByCategory(
        vararg keywords: String
    ): MutableList<Book> {
        return allBooks.filter { book ->
            keywords.any { key ->
                book.category.contains(key, ignoreCase = true) ||
                        book.genres.any { it.contains(key, ignoreCase = true) }
            }
        }.toMutableList()
    }

    // ===================== DISPLAY =====================

    private fun updateDisplay() {
        binding.contentLayout.removeAllViews()

        if (filteredBooks.isEmpty()) {
            showEmpty()
            return
        }

        val categorizedBooks = filteredBooks.groupBy { it.category }
        displayCategories(categorizedBooks)
    }

    private fun displayCategories(categorizedBooks: Map<String, List<Book>>) {
        binding.contentLayout.removeAllViews()

        categorizedBooks.forEach { (category, books) ->
            addCategorySection(category, books)
        }
    }

    private fun addCategorySection(category: String, books: List<Book>) {
        val categoryView = LayoutInflater.from(this)
            .inflate(R.layout.item_category_section, binding.contentLayout, false)

        val tvCategoryTitle =
            categoryView.findViewById<TextView>(R.id.tvCategoryTitle)
        val rvBooks =
            categoryView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvBooks)

        tvCategoryTitle.text = category

        rvBooks.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        rvBooks.adapter = BookAdapter(books) { book ->
            Intent(this, DetailBookActivity::class.java).apply {
                putExtra("BOOK_ID", book.id)
                putExtra("BOOK_TITLE", book.title)
                putExtra("BOOK_AUTHOR", book.author)
                putExtra("BOOK_COVER", book.coverUrl)
                putExtra("BOOK_RATING", book.rating)
                putExtra("BOOK_YEAR", book.publishedYear)
                putExtra("BOOK_PAGES", book.pages)
                putExtra("BOOK_SYNOPSIS", book.synopsis)
                putExtra("BOOK_PRICE", book.price)
                putStringArrayListExtra(
                    "BOOK_GENRES",
                    ArrayList(book.genres)
                )
                startActivity(this)
            }
        }

        binding.contentLayout.addView(categoryView)
    }

    // ===================== UI STATE =====================

    private fun showLoading(show: Boolean) {
        isLoading = show
        binding.progressBar.visibility =
            if (show) View.VISIBLE else View.GONE
    }

    private fun showEmpty() {
        val emptyView = TextView(this).apply {
            text = "Tidak ada buku yang ditemukan"
            textSize = 16f
            setTextColor(getColor(android.R.color.darker_gray))
            setPadding(32, 64, 32, 32)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
        binding.contentLayout.addView(emptyView)
    }

    private fun showError(message: String) {
        val errorView = TextView(this).apply {
            text = message
            textSize = 16f
            setTextColor(getColor(android.R.color.holo_red_dark))
            setPadding(32, 64, 32, 32)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
        binding.contentLayout.addView(errorView)
    }
}
