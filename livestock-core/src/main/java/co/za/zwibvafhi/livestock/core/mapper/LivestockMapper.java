package co.za.zwibvafhi.livestock.core.mapper;

import co.za.zwibvafhi.livestock.api.dto.ExpenseResponse;
import co.za.zwibvafhi.livestock.api.dto.ExpenseSummaryResponse;
import co.za.zwibvafhi.livestock.api.dto.InitializeCountRequest;
import co.za.zwibvafhi.livestock.api.dto.LivestockCountResponse;
import co.za.zwibvafhi.livestock.api.dto.LivestockEventResponse;
import co.za.zwibvafhi.livestock.api.dto.RecordEventRequest;
import co.za.zwibvafhi.livestock.api.dto.RecordExpenseRequest;
import co.za.zwibvafhi.livestock.api.model.ExpenseCategory;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockCount;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockEvent;
import co.za.zwibvafhi.livestock.persistence.entity.LivestockExpense;
import co.za.zwibvafhi.livestock.persistence.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between DTOs and entities.
 * */
@Component
public class LivestockMapper {

  public LivestockCount toCount(User user, InitializeCountRequest request) {
    LivestockCount count = new LivestockCount();
    count.setUser(user);
    count.setCategory(request.getCategory());
    count.setMaleCount(request.getMaleCount());
    count.setFemaleCount(request.getFemaleCount());
    count.setMaxId(request.getMaleCount() + request.getFemaleCount());
    return count;
  }

  public LivestockCountResponse toCountResponse(LivestockCount count) {
    return LivestockCountResponse.builder()
        .userId(count.getUser().getUserId())
        .category(count.getCategory())
        .maleCount(count.getMaleCount())
        .femaleCount(count.getFemaleCount())
        .build();
  }

  public LivestockEvent toEvent(User user, RecordEventRequest request) {
    LivestockEvent event = new LivestockEvent();
    event.setUser(user);
    event.setCategory(request.getCategory());
    event.setEventType(request.getEventType());
    event.setMaleCount(request.getMaleCount());
    event.setFemaleCount(request.getFemaleCount());
    event.setEventDate(LocalDate.now());
    event.setSalePrice(request.getSalePrice());
    event.setCost(request.getCost());
    return event;
  }

  public LivestockEventResponse toEventResponse(LivestockEvent event) {
    return LivestockEventResponse.builder()
        .id(event.getId())
        .userId(event.getUser().getUserId())
        .category(event.getCategory())
        .eventType(event.getEventType())
        .maleCount(event.getMaleCount())
        .femaleCount(event.getFemaleCount())
        .eventDate(event.getEventDate())
        .salePrice(event.getSalePrice())
        .cost(event.getCost())
        .build();
  }

  public LivestockExpense toExpense(User user, RecordExpenseRequest request) {
    LivestockExpense expense = new LivestockExpense();
    expense.setUser(user);
    expense.setCategory(request.getCategory());
    expense.setExpenseCategory(request.getExpenseCategory());
    expense.setAmount(request.getAmount());
    expense.setDescription(request.getDescription());
    expense.setExpenseDate(LocalDate.now());
    return expense;
  }

  public ExpenseResponse toExpenseResponse(LivestockExpense expense) {
    return ExpenseResponse.builder()
        .id(expense.getId())
        .userId(expense.getUser().getUserId())
        .category(expense.getCategory())
        .expenseCategory(expense.getExpenseCategory())
        .amount(expense.getAmount())
        .description(expense.getDescription())
        .date(expense.getExpenseDate())
        .build();
  }

  public ExpenseSummaryResponse toExpenseSummaryResponse(Object[] summary) {
    return ExpenseSummaryResponse.builder()
        .expenseCategory((ExpenseCategory) summary[0])
        .totalAmount((BigDecimal) summary[1])
        .expenseCount(((Number) summary[2]).longValue())
        .build();
  }
}
