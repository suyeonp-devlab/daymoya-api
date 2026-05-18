package com.app.daymoya.domain.tasks.service;

import com.app.daymoya.domain.categories.entity.Category;
import com.app.daymoya.domain.categories.entity.CategoryScope;
import com.app.daymoya.domain.categories.repository.CategoryRepository;
import com.app.daymoya.domain.tasks.dto.request.TaskCreateRequest;
import com.app.daymoya.domain.tasks.dto.request.TaskStatusChangeRequest;
import com.app.daymoya.domain.tasks.dto.request.TaskUpdateRequest;
import com.app.daymoya.domain.tasks.dto.response.TaskResponse;
import com.app.daymoya.domain.tasks.entity.Task;
import com.app.daymoya.domain.tasks.entity.TaskStatus;
import com.app.daymoya.domain.tasks.repository.TaskRepository;
import com.app.daymoya.global.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.daymoya.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

  private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

  private final TaskRepository taskRepository;
  private final CategoryRepository categoryRepository;

  /** 개인 일정 목록 조회 */
  public List<TaskResponse> getPersonalTasks(Long userId, String yearMonth, TaskStatus status) {

    // 개인 일정 목록 조회
    LocalDateTime[] range = parseYearMonth(yearMonth);
    List<Task> tasks = taskRepository.findPersonalTasks(userId, range[0], range[1], status);

    // 카테고리명 조회
    Map<Long, String> categoryNames = fetchCategoryNames(tasks);

    return tasks.stream()
      .map(t -> TaskResponse.from(t, categoryNames.get(t.getCategoryId())))
      .toList();
  }

  /** 개인 일정 생성 */
  @Transactional
  public void createPersonalTask(Long userId, TaskCreateRequest request) {

    // 카테고리 ID 검증
    validatePersonalCategory(userId, request.getCategoryId());

    // 시작일자 세팅
    LocalDateTime startAt = request.getStartAt() != null
        ? request.getStartAt()
        : request.getEndAt().toLocalDate().atStartOfDay();

    // 일정 생성
    Task task = Task.createPersonal(
      userId,
      request.getTitle(),
      request.getDescription(),
      request.getPriority(),
      startAt,
      request.getEndAt(),
      request.getCategoryId()
    );

    taskRepository.save(task);
  }

  /** 개인 일정 수정 */
  @Transactional
  public void updatePersonalTask(Long userId, Long taskId, TaskUpdateRequest request) {

    // 카테고리 ID 검증
    validatePersonalCategory(userId, request.getCategoryId());

    // 시작일자 세팅
    LocalDateTime startAt = request.getStartAt() != null
      ? request.getStartAt()
      : request.getEndAt().toLocalDate().atStartOfDay();

    // 일정 수정
    Task task = findPersonalTask(userId, taskId);
    task.update(
      request.getTitle(),
      request.getDescription(),
      request.getPriority(),
      startAt,
      request.getEndAt(),
      request.getCategoryId()
    );
  }

  /** 개인 일정 삭제 */
  @Transactional
  public void deletePersonalTask(Long userId, Long taskId) {
    Task task = findPersonalTask(userId, taskId);
    task.softDelete();
  }

  /** 개인 일정 상태 변경 */
  @Transactional
  public void changePersonalTaskStatus(Long userId, Long taskId, TaskStatusChangeRequest request) {
    Task task = findPersonalTask(userId, taskId);
    task.changeStatus(request.getStatus());
  }

  /** 개인 일정 단건 조회 */
  private Task findPersonalTask(Long userId, Long taskId) {

    Task task = taskRepository.findById(taskId)
      .orElseThrow(() -> new BizException(TASK_NOT_FOUND));

    if (task.isDeleted()) throw new BizException(TASK_NOT_FOUND);
    if (!task.isPersonal(userId)) throw new BizException(TASK_ACCESS_DENIED);
    return task;
  }

  /** 문자열(yyyyMM) → 조회 범위 [시작일시, 종료일시] parsing */
  private LocalDateTime[] parseYearMonth(String yearMonth) {

    try {
      YearMonth ym = YearMonth.parse(yearMonth, YEAR_MONTH_FORMATTER);
      LocalDateTime from = ym.atDay(1).atStartOfDay();
      LocalDateTime to =  ym.plusMonths(1).atDay(1).atStartOfDay();
      return new LocalDateTime[]{from, to};

    } catch (DateTimeParseException e) {
      throw new BizException(INVALID_VALUE);
    }
  }

  /** 개인 일정 > 카테고리 ID 유효성 검증 */
  private void validatePersonalCategory(Long userId, Long categoryId) {

    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new BizException(CATEGORY_NOT_FOUND));

    if (!category.isPersonal(userId) && category.getScope() != CategoryScope.SYS_PERSONAL) {
      throw new BizException(CATEGORY_ACCESS_DENIED);
    }
  }

  /** 개인 일정 > 카테고리명 조회 */
  private Map<Long, String> fetchCategoryNames(List<Task> tasks) {

    Set<Long> categoryIds = tasks.stream()
      .map(Task::getCategoryId)
      .filter(Objects::nonNull)
      .collect(Collectors.toSet());

    if (categoryIds.isEmpty()) return Collections.emptyMap();

    return categoryRepository.findAllById(categoryIds).stream()
      .collect(Collectors.toMap(Category::getId, Category::getName));
  }

}
