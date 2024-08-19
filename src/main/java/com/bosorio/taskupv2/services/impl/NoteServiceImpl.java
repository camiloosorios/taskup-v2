package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Note;
import com.bosorio.taskupv2.repositories.NoteRepository;
import com.bosorio.taskupv2.services.NoteService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bosorio.taskupv2.utils.ModelConverter.dtoToNote;
import static com.bosorio.taskupv2.utils.ModelConverter.noteToDTO;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final UserService userService;

    public NoteServiceImpl(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void createNote(Long userId, TaskDTO taskDTO, NoteDTO noteDTO) {
        UserDTO userDTO = userService.getUser(userId);
        noteDTO.setTask(taskDTO);
        noteDTO.setCreatedBy(userDTO);
        Note note = dtoToNote(noteDTO);
        try {
            noteRepository.save(note);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public List<NoteDTO> getTaskNotes(Long taskId) {
        List<Note> notes = noteRepository.findByTaskId(taskId);
        List<NoteDTO> noteDTOs = new ArrayList<>();
        notes.forEach(note -> noteDTOs.add(noteToDTO(note)));
        return noteDTOs;
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Note not found"));
        try {
            noteRepository.delete(note);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
