package com.example.book_management.repository;

import com.example.book_management.model.Programare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Programare,Long> {
}
