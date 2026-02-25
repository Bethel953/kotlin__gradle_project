package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "7" -> {
            listAllPatrons(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

private fun addBook(service: LibraryService) {
    println("Enter ISBN:")
    val isbn = readln()

    println("Enter title:")
    val title = readln()

    println("Enter author:")
    val author = readln()

    println("Enter publication year:")
    val year = readln().toInt()

    val book = Book(isbn, title, author, year)

    if (service.addBook(book)) {
        println("Book added successfully.")
    } else {
        println("Book already exists.")
    }
}

private fun registerPatron(repository: LibraryRepository) {
    println("Enter patron ID:")
    val id = readln()

    println("Enter patron name:")
    val name = readln()

    println("Enter email:")
    val email = readln()

    println("Enter phone:")
    val phone = readln()

    val patron = Patron(id, name, email, phone)

    if (repository.addPatron(patron)) {
        println("Patron registered successfully.")
    } else {
        println("Patron already exists.")
    }
}

private fun borrowBook(service: LibraryService) {
    println("Enter patron ID:")
    val patronId = readln()

    println("Enter ISBN:")
    val isbn = readln()

    when (service.borrowBook(patronId, isbn)) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully.")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is not available.")
        BorrowResult.LIMIT_REACHED -> println("Borrow limit reached.")
    }
}

private fun returnBook(service: LibraryService) {
    println("Enter patron ID:")
    val patronId = readln()

    println("Enter ISBN:")
    val isbn = readln()

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully.")
    } else {
        println("Return failed.")
    }
}

private fun search(service: LibraryService) {
    println("Enter search query:")
    val query = readln()

    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found.")
    } else {
        results.forEach {
            println("${it.isbn} - ${it.title} by ${it.author} (Available: ${it.isAvailable})")
        }
    }
}

private fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()

    if (books.isEmpty()) {
        println("No books in library.")
    } else {
        books.forEach {
            println("${it.isbn} - ${it.title} by ${it.author} (Available: ${it.isAvailable})")
        }
    }
}

private fun listAllPatrons(repository: LibraryRepository) {
    val patrons = (repository as? org.dbu.library.repository.InMemoryLibraryRepository)
        ?.getAllPatrons()

    if (patrons == null || patrons.isEmpty()) {
        println("No patrons registered.")
    } else {
        patrons.forEach {
            println("${it.id} - ${it.name}")
        }
    }
}