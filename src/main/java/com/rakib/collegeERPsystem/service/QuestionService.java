package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.dto.exam.OptionChoiceDTO;
import com.rakib.collegeERPsystem.dto.exam.QuestionDTO;
import com.rakib.collegeERPsystem.entity.exam.Exam;
import com.rakib.collegeERPsystem.entity.exam.OptionChoice;
import com.rakib.collegeERPsystem.entity.exam.Question;
import com.rakib.collegeERPsystem.repository.exam.ExamRepository;
import com.rakib.collegeERPsystem.repository.exam.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    /**
     * Create a new question for a given exam
     */
    public QuestionDTO createQuestion(Long examId, QuestionDTO questionDTO) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + examId));

        Question question = Question.builder()
                .questionText(questionDTO.getQuestionText())
                .marks(questionDTO.getMarks())
                .correctAnswer(questionDTO.getCorrectAnswer())
                .exam(exam)
                .build();

        List<OptionChoice> options = questionDTO.getOptions().stream()
                .map(opt -> OptionChoice.builder()
                        .optionText(opt.getOptionText())
                        .optionLabel(opt.getOptionLabel())
                        .question(question)
                        .build())
                .collect(Collectors.toList());

        question.setOptions(options);
        Question savedQuestion = questionRepository.save(question);

        return convertToDTO(savedQuestion);
    }

    /**
     * Get all questions for a specific exam
     */
//    @Transactional(readOnly = true)
//    public List<QuestionDTO> getQuestionsByExam(Long examId) {
////        List<Question> questions = questionRepository.findByExam_ExamId(examId);
//        return questions.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    /**
     * Update a question
     */
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));

        existingQuestion.setQuestionText(questionDTO.getQuestionText());
        existingQuestion.setMarks(questionDTO.getMarks());
        existingQuestion.setCorrectAnswer(questionDTO.getCorrectAnswer());

        // Update options
        existingQuestion.getOptions().clear();
        List<OptionChoice> updatedOptions = questionDTO.getOptions().stream()
                .map(opt -> OptionChoice.builder()
                        .optionText(opt.getOptionText())
                        .optionLabel(opt.getOptionLabel())
                        .question(existingQuestion)
                        .build())
                .collect(Collectors.toList());

        existingQuestion.getOptions().addAll(updatedOptions);

        Question updated = questionRepository.save(existingQuestion);
        return convertToDTO(updated);
    }

    /**
     * Delete a question
     */
    public void deleteQuestion(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question not found with ID: " + questionId);
        }
        questionRepository.deleteById(questionId);
    }

    /**
     * Convert Question → QuestionDTO
     */
    private QuestionDTO convertToDTO(Question question) {
        return QuestionDTO.builder()
                .questionId(question.getQuestionId())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .correctAnswer(question.getCorrectAnswer())
                .options(
                        question.getOptions() != null ?
                                question.getOptions().stream()
                                        .map(opt -> OptionChoiceDTO.builder()
                                                .optionId(opt.getOptionId())
                                                .optionLabel(opt.getOptionLabel())
                                                .optionText(opt.getOptionText())
                                                .build())
                                        .collect(Collectors.toList())
                                : null
                )
                .build();
    }
}