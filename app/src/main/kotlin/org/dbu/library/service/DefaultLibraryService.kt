package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.LibraryRepository

class DefaultLibraryService(
    private val repository: LibraryRepository
) : LibraryService {

    private val borrowLimit = 3

    override fun addBook(book: Book): Boolean {
        return repository.addBook(book)
    }

    override fun borrowBook(patronId: String, isbn: String): BorrowResult {

        val patron = repository.findPatron(patronId)
            ?: return BorrowResult.PATRON_NOT_FOUND

        val book = repository.findBook(isbn)
            ?: return BorrowResult.BOOK_NOT_FOUND

        if (!book.isAvailable) {
            return BorrowResult.NOT_AVAILABLE
        }

        val borrowedCount = repository.getAllBooks()
            .count { !it.isAvailable }

        if (borrowedCount >= borrowLimit) {
            return BorrowResult.LIMIT_REACHED
        }

        repository.updateBook(book.copy(isAvailable = false))

        return BorrowResult.SUCCESS
    }

    override fun returnBook(patronId: String, isbn: String): Boolean {

        val book = repository.findBook(isbn) ?: return false

        if (book.isAvailable) return false

        repository.updateBook(book.copy(isAvailable = true))

        return true
    }

    override fun search(query: String): List<Book> {
        return repository.getAllBooks().filter {
            it.title.contains(query, true) ||
            it.author.contains(query, true)
        }
    }
}