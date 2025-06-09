package app.task_manager.task.aggregate;

import app.task_manager.User.aggregate.UserAggregateService;
import app.task_manager.User.entity.UserEntity;
import app.task_manager.tag.dto.TagDTO;
import app.task_manager.tag.entity.TagEntity;
import app.task_manager.tag.exception.TagNotFoundException;
import app.task_manager.tag.mapper.TagMapper;
import app.task_manager.tag.repository.TagRepository;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task.exception.TaskNotFoundException;
import app.task_manager.task.repository.TaskRepository;
import app.task_manager.taskAttribute.entity.TaskAttributeEntity;
import app.task_manager.taskAttribute.exception.TaskAttributeNotFoundException;
import app.task_manager.taskAttribute.repository.TaskAttributeRepository;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import app.task_manager.task_comment.exception.TaskCommentNotFoundException;
import app.task_manager.task_comment.mapper.TaskCommentMapper;
import app.task_manager.task_comment.repository.TaskCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAggregateService {

    private final TaskRepository taskRepository;
    private final UserAggregateService userService;
    private final TagRepository tagRepository;
    private final TaskAttributeRepository taskAttributeRepository;
    private final TagMapper tagMapper;
    private final TaskCommentMapper taskCommentMapper;
    private final TaskCommentRepository taskCommentRepository;

    @Transactional
    public void assignUser(Long taskId, Long userId) {
        log.info("assignUser – starting: taskId={}, userId={}", taskId, userId);
        TaskEntity task = loadTask(taskId);
        UserEntity user = userService.getAggregateByUserId(userId).getUserEntity();
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.assignUser(user);
        taskRepository.save(task);
        log.info("assignUser – completed: userId={} assigned to taskId={}", userId, taskId);
    }


    @Transactional
    public void removeAssignedUser(Long taskId, Long userId) {
        log.info("removeAssignedUser – starting: taskId={}, userId={}", taskId, userId);
        TaskEntity task = loadTask(taskId);
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.removeAssignedUser(userId);
        taskRepository.save(task);
        log.info("removeAssignedUser – completed: userId={} removed from taskId={}", userId, taskId);
    }

    @Transactional
    public void changePriority(Long taskId, Long priorityId) {
        log.info("changePriority – starting: taskId={}, priorityId={}", taskId, priorityId);
        TaskEntity task = loadTask(taskId);
        TaskAttributeEntity priority = taskAttributeRepository.findById(priorityId).orElseThrow(() -> {
            log.error("changePriority – priority not found: priorityId={}", priorityId);
            return new TaskAttributeNotFoundException("Priority with id: " + priorityId + " was not found");
        });
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.changePriority(priority);
        taskRepository.save(task);
        log.info("changePriority – completed: priorityId={} set on taskId={}", priorityId, taskId);
    }

    @Transactional
    public void changeStatus(Long taskId, Long statusId) {
        log.info("changeStatus – starting: taskId={}, statusId={}", taskId, statusId);
        TaskEntity task = loadTask(taskId);
        TaskAttributeEntity status = taskAttributeRepository.findById(statusId).orElseThrow(() -> {
            log.error("changeStatus – status not found: statusId={}", statusId);
            return new TaskAttributeNotFoundException("Status with id: " + statusId + " was not found");
        });
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.changeStatus(status);
        taskRepository.save(task);
        log.info("changeStatus – completed: statusId={} set on taskId={}", statusId, taskId);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> getTags(Long taskId) {
        log.debug("getTags – fetching tags for taskId={}", taskId);
        TaskEntity task = loadTask(taskId);
        List<TagDTO> dtos = new TaskAggregate(task).getTags().stream().map(tagMapper::toDTO).toList();
        log.debug("getTags – found {} tags for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    @Transactional
    public void assignTag(Long taskId, Long tagId) {
        log.info("assignTag – starting: taskId={}, tagId={}", taskId, tagId);
        TaskEntity task = loadTask(taskId);
        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> {
            log.error("assignTag – tag not found: tagId={}", tagId);
            return new TagNotFoundException("Tag not found: " + tagId);
        });
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.assignTag(tag);
        taskRepository.save(task);
        log.info("assignTag – completed: tagId={} assigned to taskId={}", tagId, taskId);
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getAllComments(Long taskId) {
        log.debug("getAllComments – fetching all comments for taskId={}", taskId);
        TaskEntity task = loadTask(taskId);
        List<TaskCommentDTO> dtos = TaskAggregate.builder().taskEntity(task).build().getComments().stream().map(taskCommentMapper::toDTO).toList();
        log.debug("getAllComments – found {} comments for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getRootComments(Long taskId) {
        log.debug("getRootComments – fetching root comments for taskId={}", taskId);
        TaskEntity task = loadTask(taskId);
        List<TaskCommentDTO> dtos = TaskAggregate.builder().taskEntity(task).build().getRootComments().stream().map(taskCommentMapper::toDTO).toList();
        log.debug("getRootComments – found {} root comments for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getChildComments(Long taskId, Long parentCommentId) {
        log.debug("getChildComments – fetching child comments for parentCommentId={} on taskId={}", parentCommentId, taskId);
        TaskEntity task = loadTask(taskId);
        TaskCommentEntity parent = taskCommentRepository.findById(parentCommentId).orElseThrow(() -> {
            log.error("getChildComments – parent comment not found: parentCommentId={}", parentCommentId);
            return new TaskCommentNotFoundException("Parent comment not found: " + parentCommentId);
        });
        List<TaskCommentDTO> dtos = TaskAggregate.builder().taskEntity(task).build().getChildComments(parent).stream().map(taskCommentMapper::toDTO).toList();
        log.debug("getChildComments – found {} child comments for parentCommentId={}", dtos.size(), parentCommentId);
        return dtos;
    }

    @Transactional
    public void unassignTag(Long taskId, Long tagId) {
        log.info("unassignTag – starting: taskId={}, tagId={}", taskId, tagId);
        TaskEntity task = loadTask(taskId);
        TaskAggregate aggregate = TaskAggregate.builder().taskEntity(task).build();
        aggregate.unassignTag(tagId);
        taskRepository.save(task);
        log.info("unassignTag – completed: tagId={} unassigned from taskId={}", tagId, taskId);
    }

    private TaskEntity loadTask(Long taskId) {
        log.debug("loadTask – loading taskId={}", taskId);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("loadTask – task not found: taskId={}", taskId);
            return new TaskNotFoundException("Task not found: " + taskId);
        });
        log.debug("loadTask – loaded taskId={}", taskId);
        return task;
    }
}
