package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.*;
import com.example.formofnationallibrary.Entities.Queue;
import com.example.formofnationallibrary.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@SessionAttributes("loggedInUser")
public class PersonController {
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final BookService bookService;
    private final QueueService queueService;
    private final CopiesService copiesService;
    private final StatusCopiesService statusCopiesService;
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;
    @Autowired
    private EmailService emailService;

    @Autowired
    public PersonController(QueueService queueService, UserService userService, FavoriteService favoriteService, BookService bookService, OrderService orderService, OrderHistoryService orderHistoryService, CopiesService copiesService, StatusCopiesService statusCopiesService) {
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.orderHistoryService = orderHistoryService;
        this.copiesService = copiesService;
        this.statusCopiesService = statusCopiesService;
        this.queueService = queueService;
    }

    @GetMapping("/person")
    public ModelAndView person(Model model) {
        ModelAndView modelAndView = new ModelAndView("Person");
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            Optional<User> userOptional = userService.findById(loggedInUser.getId());
            userOptional.ifPresent(user -> model.addAttribute("loggedInUserDetails", user));
        } else {
            model.addAttribute("loggedIn", false);
        }
        return modelAndView;
    }

    @GetMapping("/getUserDetails")
    @ResponseBody
    public Map<String, Object> getUserDetails(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Optional<User> userOptional = userService.findById(loggedInUser.getId());
            userOptional.ifPresent(user -> response.put("user", user));
        }
        return response;
    }

    @GetMapping("/getFavorites")
    @ResponseBody
    public Map<String, Object> getFavorites(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<Favorite> favorites = favoriteService.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> favoriteBooks = favorites.stream()
                    .map(favorite -> {
                        Map<String, Object> favoriteBook = new HashMap<>();
                        favoriteBook.put("id", favorite.getId());
                        Book book = bookService.findById(favorite.getBookId());
                        favoriteBook.put("bookName", book != null ? book.getName() : "Неизвестно");
                        return favoriteBook;
                    })
                    .collect(Collectors.toList());
            response.put("favorites", favoriteBooks);
        }
        return response;
    }
    @PostMapping("/removeFromFavorites")
    @ResponseBody
    public Map<String, Object> removeFromFavorites(@RequestBody Map<String, Long> request) {
        Map<String, Object> response = new HashMap<>();
        Long favoriteId = request.get("id");
        favoriteService.removeById(favoriteId);
        response.put("success", true);
        return response;
    }
    @GetMapping("/getOrders")
    @ResponseBody
    public Map<String, Object> getOrders(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<Order> orders = orderService.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> orderDetails = orders.stream()
                    .map(order -> {
                        Map<String, Object> orderDetail = new HashMap<>();
                        Book book = order.getCopies().getBook();
                        orderDetail.put("id", order.getId());
                        orderDetail.put("bookName", book != null ? book.getName() : "Неизвестно");
                        orderDetail.put("cipher", order.getCopies().getCipher());
                        orderDetail.put("issueDate", order.getIssueDate());
                        orderDetail.put("returnDate", order.getReturnDate());
                        return orderDetail;
                    })
                    .collect(Collectors.toList());
            response.put("orders", orderDetails);
        }
        return response;
    }

    @PostMapping("/removeOrder")
    @ResponseBody
    public Map<String, Object> removeOrder(@RequestBody Map<String, Long> request) {
        Map<String, Object> response = new HashMap<>();
        Long orderId = request.get("id");
        Order order = orderService.findById(orderId);

        if (order != null) {
            // Получаем экземпляр книги из заказа
            Copies copies = order.getCopies();
            Long bookId = copies.getBook().getId();

            // Проверяем наличие очереди на данную книгу
            List<Queue> queueList = queueService.findQueueByBookIdOrderByQueueNumberAsc(bookId);

            if (queueList.isEmpty()) {
                // Если очереди нет, выполняем стандартное удаление заказа и обновление статуса
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setUser(userService.findById(order.getUserId()).orElse(null));
                orderHistory.setCopies(order.getCopies());
                orderHistory.setIssueDate(order.getIssueDate());
                orderHistory.setReturnDate(order.getReturnDate());
                orderHistory.setReturnDate(LocalDate.now()); // Текущая дата
                orderHistoryService.save(orderHistory);

                StatusCopies freeStatus = statusCopiesService.findByStatus("Свободен");
                copies.setStatusCopies(freeStatus);
                copiesService.saveCopy(copies);

                orderService.removeById(orderId);
                response.put("success", true);
            } else {
                // Если очередь существует, обрабатываем первого пользователя в очереди
                Queue firstInQueue = queueList.get(0);
                User nextUser = userService.findById(firstInQueue.getUserId()).orElse(null);
                if (nextUser != null) {
                    // Создаем новый заказ для первого пользователя в очереди
                    Order newOrder = new Order();
                    newOrder.setUserId(nextUser.getId());
                    newOrder.setCopies(copies);
                    newOrder.setIssueDate(LocalDate.now());
                    newOrder.setReturnDate(LocalDate.now().plusDays(30));
                    orderService.save(newOrder);

                    // Обновляем статус экземпляра книги на "Занят"
                    StatusCopies occupiedStatus = statusCopiesService.findByStatus("Занят");
                    copies.setStatusCopies(occupiedStatus);
                    copiesService.saveCopy(copies);

                    // Удаляем запись из очереди
                    queueService.remove(firstInQueue);

                    // Обновляем queue_number для оставшихся пользователей
                    for (int i = 1; i < queueList.size(); i++) {
                        Queue queue = queueList.get(i);
                        queue.setQueueNumber(queue.getQueueNumber() - 1);
                        queueService.save(queue);
                    }
                    orderService.removeById(orderId);
                    // Отправляем уведомление пользователю
                    //emailService.sendEmail(nextUser.getEmail(), "Your Book Order", "You have successfully ordered the book.");

                    response.put("success", true);
                } else {
                    response.put("success", false);
                }
            }
        } else {
            response.put("success", false);
        }

        return response;
    }

    @GetMapping("/getOrderHistory")
    @ResponseBody
    public Map<String, Object> getOrderHistory(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<OrderHistory> orderHistoryList = orderHistoryService.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> orderHistoryData = orderHistoryList.stream()
                    .map(orderHistory -> {
                        Map<String, Object> order = new HashMap<>();
                        Book book = bookService.findById(orderHistory.getCopies().getBook().getId());
                        order.put("bookName", book != null ? book.getName() : "Неизвестно");
                        order.put("cipher", orderHistory.getCopies().getCipher());
                        order.put("issueDate", orderHistory.getIssueDate());
                        order.put("returnDate", orderHistory.getReturnDate());
                        return order;
                    })
                    .collect(Collectors.toList());
            response.put("orderHistory", orderHistoryData);
        }
        return response;
    }


    @GetMapping("/getQueue")
    @ResponseBody
    public Map<String, Object> getQueue(Model model) {
        Map<String, Object> response = new HashMap<>();
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<Queue> queue = queueService.findByUserId(loggedInUser.getId());
            List<Map<String, Object>> queueData = queue.stream()
                    .map(item -> {
                        Map<String, Object> queueItem = new HashMap<>();
                        Book book = bookService.findById(item.getBook().getId());
                        queueItem.put("bookName", book != null ? book.getName() : "Неизвестно");
                        queueItem.put("queueNumber", item.getQueueNumber());
                        queueItem.put("queueId", item.getId());
                        return queueItem;
                    })
                    .collect(Collectors.toList());
            response.put("queue", queueData);
        }
        return response;
    }

    @PostMapping("/leaveQueue")
    @ResponseBody
    public Map<String, Object> leaveQueue(@RequestBody Map<String, Long> request) {
        Map<String, Object> response = new HashMap<>();
        Long queueId = request.get("id");

        // Извлекаем объект из Optional
        Optional<Queue> optionalQueueItem = queueService.findById(queueId);
        if (optionalQueueItem.isPresent()) {
            Queue queueItem = optionalQueueItem.get();
            Long bookId = queueItem.getBook().getId();
            int removedQueueNumber = queueItem.getQueueNumber();

            // Находим все записи в очереди для данной книги с номером очереди больше удаленного
            List<Queue> remainingQueueItems = queueService.findByBookIdAndQueueNumberGreaterThan(bookId, removedQueueNumber);

            // Уменьшаем номер очереди на 1 для всех найденных записей
            for (Queue item : remainingQueueItems) {
                item.setQueueNumber(item.getQueueNumber() - 1);
                queueService.save(item);
            }

            // Удаляем целевую запись из очереди
            queueService.removeById(queueId);

            response.put("success", true);
        } else {
            response.put("success", false);
        }
        return response;
    }

}

