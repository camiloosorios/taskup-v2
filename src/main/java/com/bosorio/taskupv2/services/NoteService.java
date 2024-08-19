package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;

public interface NoteService {

    void createNote(Long userId, TaskDTO taskDTO, NoteDTO noteDTO);

    void deleteNote(NoteDTO noteDTO);

}
