package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.*;
import com.example.formofnationallibrary.Repository.BookRepository;
import com.example.formofnationallibrary.Repository.CopiesRepository;
import com.example.formofnationallibrary.Repository.OrderRepository;
import com.example.formofnationallibrary.Repository.QueueRepository;
import com.example.formofnationallibrary.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("loggedInUser")
public class BookController {

    @Autowired
    private QueueService queueService;
    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private LanguageService languageService;
    @Autowired
    private StatusCopiesService statusCopiesService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PublishService publishService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopiesRepository copiesRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QueueRepository queueRepository;
    @GetMapping("/book/{id}")
    public ModelAndView getBook(@PathVariable Long id, Model model) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isPresent()) {
            User loggedInUser = (User) model.getAttribute("loggedInUser");
            if (loggedInUser != null) {
                model.addAttribute("loggedIn", true);
            } else {
                model.addAttribute("loggedIn", false);
            }

            ModelAndView mav = new ModelAndView("book");
            Book book = bookOptional.get();
            mav.addObject("book", book);

            // Получение всех копий для данной книги
            List<Copies> copiesList = copiesRepository.findByBookId(book.getId());
            mav.addObject("copiesList", copiesList);

            return mav;
        } else {
            return new ModelAndView("error/404");
        }
    }

    @GetMapping("/addBook")
    public ModelAndView showAddBookForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);

            ModelAndView mav = new ModelAndView("AddBook");
            mav.addObject("book", new Book());

            List<Author> sortedAuthors = authorService.getAllAuthors().stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .collect(Collectors.toList());
            mav.addObject("authors", sortedAuthors);

            List<Language> sortedLanguages = languageService.getAllLanguages().stream()
                    .sorted(Comparator.comparing(Language::getName))
                    .collect(Collectors.toList());
            mav.addObject("languages", sortedLanguages);

            List<Publish> sortedPublishes = publishService.getAllPublishes().stream()
                    .sorted(Comparator.comparing(Publish::getName))
                    .collect(Collectors.toList());
            mav.addObject("publishes", sortedPublishes);

            return mav;
        } else {
            // Если пользователь не администратор или не авторизован, перенаправляем его куда-то еще
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/addBook")
    public ModelAndView addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/addAuthor")
    public ModelAndView showAddAuthorForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
        model.addAttribute("loggedIn", true);
        ModelAndView mav = new ModelAndView("AddAuthor");
        mav.addObject("author", new Author());
        return mav;
        }else{
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/addAuthor")
    public ModelAndView addAuthor(@ModelAttribute Author author) {
        authorService.saveAuthor(author);
        return new ModelAndView("redirect:/addBook");
    }

    @GetMapping("/addPublish")
    public ModelAndView showAddPublishForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            ModelAndView mav = new ModelAndView("AddPublish");
            mav.addObject("publish", new Publish());
            return mav;
        }else{
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/addPublish")
    public ModelAndView addPublish(@ModelAttribute Publish publish) {
        publishService.savePublish(publish);
        return new ModelAndView("redirect:/addBook");
    }
    @GetMapping("/addLanguage")
    public ModelAndView showAddLanguageForm(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);
            ModelAndView mav = new ModelAndView("AddLanguage");
            mav.addObject("language", new Language());
            return mav;
        }else{
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/addLanguage")
    public ModelAndView addLa(@ModelAttribute Language language) {
        languageService.saveLanguage(language);
        return new ModelAndView("redirect:/addBook");
    }

    @GetMapping("/addCopies")
    public ModelAndView showAddCopiesForm(Model model, @RequestParam Long bookId) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedIn", true);

            ModelAndView mav = new ModelAndView("AddCopies");
            mav.addObject("copies", new Copies());
            mav.addObject("bookId", bookId);

            List<StatusCopies> sortedStatus = statusCopiesService.getAllStatusCopies();
            mav.addObject("status", sortedStatus);

            List<Location> sortedLocation = locationService.getAllLocation().stream()
                    .sorted(Comparator.comparing(Location::getPlace))
                    .collect(Collectors.toList());
            mav.addObject("locations", sortedLocation);

            return mav;
        } else {
            // Если пользователь не администратор или не авторизован, перенаправляем его куда-то еще
            return new ModelAndView("redirect:/search");
        }
    }

    @PostMapping("/addCopies")
    public ModelAndView addCopies(@ModelAttribute Copies copies, @RequestParam Long bookId) {
        // Установка книги для копии
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            // Обработка случая, если книга не найдена
            return new ModelAndView("error/404");
        }
        copies.setBook(book);

        // Сохранение копии в базу данных
        copiesRepository.save(copies);

        // Перенаправление на страницу книги с обновленным списком копий
        return new ModelAndView("redirect:/book/" + bookId);
    }

    @PostMapping("/deleteCopie")
    public ModelAndView deleteCopie(@RequestParam Long copiesId) {
        Optional<Copies> copiesOptional = copiesRepository.findById(copiesId);
        if (copiesOptional.isPresent()) {
            Copies copy = copiesOptional.get();
            copiesRepository.delete(copy);
            return new ModelAndView("redirect:/book/" + copy.getBook().getId()); // Перенаправляем обратно на страницу книги
        } else {
            return new ModelAndView("error/404");
        }
    }

    @PostMapping("/orderBook")
    public ModelAndView orderBook(@RequestParam Long copiesId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return new ModelAndView("redirect:/login");
        }

        Optional<Copies> copiesOptional = copiesRepository.findById(copiesId);
        if (copiesOptional.isPresent()) {
            Copies copy = copiesOptional.get();
            if ("Свободен".equals(copy.getStatusCopies().getStatus())) {
                Order order = new Order();
                order.setUserId(loggedInUser.getId());
                order.setCopies(copy);
                order.setIssueDate(LocalDate.now());
                order.setReturnDate(LocalDate.now().plusDays(30));
                orderRepository.save(order);

                StatusCopies occupiedStatus = statusCopiesService.findByStatus("Занят");
                copy.setStatusCopies(occupiedStatus);
                copiesRepository.save(copy);

                return new ModelAndView("redirect:/book/" + copy.getBook().getId());
            } else {
                List<Queue> queueList = queueService.findQueueByBookId(copy.getBook().getId());
                int queueNumber = queueList.isEmpty() ? 1 : queueList.get(0).getQueueNumber() + 1;

                Queue queue = new Queue();
                queue.setUserId(loggedInUser.getId());
                queue.setBook(copy.getBook());
                queue.setQueueNumber(queueNumber);
                queueService.save(queue);

                return new ModelAndView("redirect:/book/" + copy.getBook().getId());
            }
        } else {
            return new ModelAndView("error/404");
        }
    }

    @PostMapping("/queueBook")
    public ModelAndView queueBook(@RequestParam Long bookId, Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            return new ModelAndView("error/404");
        }
        Book book = bookOptional.get();

        // Проверяем, есть ли пользователь уже в очереди на данную книгу
        Optional<Queue> existingQueueOptional = queueRepository.findByUserIdAndBookId(loggedInUser.getId(), bookId);
        if (existingQueueOptional.isPresent()) {
            // Если пользователь уже в очереди, не создаем новую запись
            return new ModelAndView("redirect:/book/" + bookId);
        }

        // Если пользователь не в очереди, создаем новую запись
        List<Queue> queueList = queueRepository.findByBookIdOrderByQueueNumberDesc(bookId);
        int queueNumber = queueList.isEmpty() ? 1 : queueList.get(0).getQueueNumber() + 1;

        Queue queue = new Queue();
        queue.setUserId(loggedInUser.getId());
        queue.setBook(book);
        queue.setQueueNumber(queueNumber);
        queue.setDateNotification(null); // или установить дату, если требуется
        queueRepository.save(queue);

        return new ModelAndView("redirect:/book/" + bookId);
    }

}

