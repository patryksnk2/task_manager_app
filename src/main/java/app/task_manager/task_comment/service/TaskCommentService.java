package app.task_manager.task_comment.service;

import app.task_manager.task_comment.aggregate.TaskCommentAggregateService;
import app.task_manager.task_comment.dto.TaskCommentCreateDTO;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import app.task_manager.task_comment.dto.TaskCommentUpdateDTO;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import app.task_manager.task_comment.mapper.TaskCommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommentService {

    private final TaskCommentAggregateService aggregateService;
    private final TaskCommentMapper mapper;

    public TaskCommentDTO create(Long taskId, TaskCommentCreateDTO createDto) {
        log.info("create – adding comment to taskId={}, parentCommentId={}", taskId, createDto.getParentCommentId());
        TaskCommentEntity entity = aggregateService.addComment(taskId, createDto.getContent(), createDto.getParentCommentId());
        TaskCommentDTO dto = mapper.toDTO(entity);
        log.info("create – comment created with id={} for taskId={}", dto.getCommentId(), taskId);
        return dto;
    }

    public void delete(Long taskId, Long commentId) {
        log.info("delete – removing commentId={} from taskId={}", commentId, taskId);
        aggregateService.removeComment(taskId, commentId);
        log.info("delete – commentId={} removed from taskId={}", commentId, taskId);
    }

    public TaskCommentDTO update(Long taskId, Long commentId, TaskCommentUpdateDTO dto) {
        log.info("update – editing commentId={} on taskId={}", commentId, taskId);
        TaskCommentEntity updated = aggregateService.editComment(taskId, commentId, dto.getContent());
        TaskCommentDTO result = mapper.toDTO(updated);
        log.info("update – commentId={} on taskId={} updated", commentId, taskId);
        return result;
    }

}
