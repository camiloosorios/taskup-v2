package com.bosorio.taskupv2.repositories;

import com.bosorio.taskupv2.entites.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
}
