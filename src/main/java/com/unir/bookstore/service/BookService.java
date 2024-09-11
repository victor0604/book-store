package com.unir.bookstore.service;

import com.unir.bookstore.data.dto.BookDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookDTO> findAll();

    Optional<BookDTO> findById(Long id);

    BookDTO save(BookDTO bookDTO);

    BookDTO update(Long id, BookDTO bookDTO);

    boolean deleteBook(Long id);

    List<BookDTO> findByTitle(String title);
}
