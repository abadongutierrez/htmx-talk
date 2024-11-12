package com.jabaddon.learning.lightningtalk.htmx_talk.todos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todos")
public class ToDosController {

    private List<ToDoItem> originalToDos = List.of(
        new ToDoItem(1L, "Buy milk"),
        new ToDoItem(2L, "Buy bread"),
        new ToDoItem(3L, "Buy eggs")
    );
    private List<ToDoItem> toDos = new ArrayList<>(originalToDos);

    @GetMapping
    public String getToDos(Model model) {
        model.addAttribute("todos", originalToDos);
        return "todos/fragments/todos";
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteToDoItem(@PathVariable Long id) {
        toDos = toDos.stream()
            .filter(toDoItem -> !toDoItem.id().equals(id))
            .toList();
        // NOTE: dont return no_content or CSS transitions aren't going to work
        return ResponseEntity.ok("");
    }
}
