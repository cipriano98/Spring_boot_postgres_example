package com.zeta.postgres.controller;

import javax.validation.Valid;

import com.zeta.postgres.model.Question;
import com.zeta.postgres.repository.QuestionRepository;
import com.zeta.postgres.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping( "/questions" )
    public Page < Question > getQuestions( Pageable pageable ) {
        return questionRepository.findAll( pageable );
    }

    @PostMapping( "/questions" )
    public Question creaQuestion( @Valid @RequestBody Question question ) {
        return questionRepository.save( question );
    }

    @PutMapping( "/questions/{questionId}" )
    public  Question updateQuestion( @PathVariable Long questionId,
                                     @Valid @RequestBody Question questionRequest ) {
        return questionRepository.findById( questionId )
        .map( question -> {
                question.setTitle( questionRequest.getTitle( ) );
                question.setDescription( questionRequest.getDescription( ) );
                return questionRepository.save( question );
            }
        ).orElseThrow( ( ) -> new ResourceNotFoundException( "Não encontrado nenhuma questão com a id: " + questionId ) );
    }
    @DeleteMapping( "/questions/{questionId}" )
    public ResponseEntity < ? >  deleteQuestion( @PathVariable Long questionId ) {
        return questionRepository.findById(questionId)
            .map( question -> {
                    questionRepository.delete( question );
                    return ResponseEntity.ok( ).build( );
                }
            ).orElseThrow( ( ) -> new ResourceNotFoundException( "Não encontrado nenhuma questão com a id: " + questionId ) );
    }

}