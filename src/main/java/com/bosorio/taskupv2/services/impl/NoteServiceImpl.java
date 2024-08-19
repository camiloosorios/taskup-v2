package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.entites.Note;
import com.bosorio.taskupv2.repositories.NoteRepository;
import com.bosorio.taskupv2.services.NoteService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static com.bosorio.taskupv2.utils.ModelConverter.dtoToNote;

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
    public void deleteNote(NoteDTO noteDTO) {

    }
}
