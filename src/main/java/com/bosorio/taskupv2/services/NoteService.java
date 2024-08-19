package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;

import java.util.List;

public interface NoteService {

    void createNote(Long userId, TaskDTO taskDTO, NoteDTO noteDTO);

    List<NoteDTO> getTaskNotes(Long taskId);

    void deleteNote(Long id);

}
