package com.unir.bookstore.service.impl;

import com.unir.bookstore.data.dto.BookDTO;
import com.unir.bookstore.data.entity.Book;
import com.unir.bookstore.data.repository.BookRepository;
import com.unir.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDTO> findAll() {
        return toDTOList(bookRepository.findAll());
    }

    @Override
    public Optional<BookDTO> findById(Long id) {

        return Optional.ofNullable(toDTO(bookRepository.findById(id).get()));
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {

        return toDTO(bookRepository.save(toEntity(bookDTO)));
    }
    
    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        Optional<Book> optionalLibro = bookRepository.findById(id);
        if (!optionalLibro.isPresent()) {
            throw new RuntimeException("El libro con id " + id + " no encontrado");
        }

        Book libroExistente = optionalLibro.get();
        libroExistente.setTitle(bookDTO.getTitle());
        libroExistente.setAuthor(bookDTO.getAuthor());
        libroExistente.setPrice(bookDTO.getPrice());

        return toDTO(bookRepository.save(libroExistente));
    }

    @Override
    public boolean deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            return !bookRepository.existsById(id);
        }
        return false;
    }

    @Override
    public List<BookDTO> findByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream()
                .map(BookServiceImpl::toDTO)
                .collect(Collectors.toList());
    }

    public static List<BookDTO> toDTOList(List<Book> books) {
        if (books == null) {
            return null;
        }

        return books.stream()
                .map(BookServiceImpl::toDTO)
                .collect(Collectors.toList());
    }

    public static BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPrice(book.getPrice());

        return bookDTO;
    }

    public static Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPrice(bookDTO.getPrice());

        return book;
    }
}
