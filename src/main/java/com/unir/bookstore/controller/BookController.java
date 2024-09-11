package com.unir.bookstore.controller;

import com.unir.bookstore.data.dto.BookDTO;
import com.unir.bookstore.service.BookService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<BookDTO> book = bookService.findById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDTO> save(@RequestBody BookDTO book) {
        BookDTO savedBook = bookService.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // Método PUT para actualizar un libro existente
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> actualizarLibro(
            @PathVariable Long id,
            @RequestBody BookDTO libroActualizado) {

        BookDTO libro = bookService.update(id, libroActualizado);
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean isRemoved = bookService.deleteBook(id);
        if (!isRemoved) {
            return (ResponseEntity<String>) ResponseEntity.notFound();
        }
        return ResponseEntity.ok("eliminado con exito");
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<BookDTO> books = bookService.findByTitle(title);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
