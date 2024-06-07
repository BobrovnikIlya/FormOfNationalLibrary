package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.*;
import com.example.formofnationallibrary.Repository.*;
import com.example.formofnationallibrary.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private CitiesService citiesService;

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
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private StatusCopiesRepository statusCopiesRepository;

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
        if (loggedInUser != null && loggedInUser.getRole().getId() == 1) {
            model.addAttribute("loggedIn", true);

            ModelAndView mav = new ModelAndView("AddBook");
            mav.addObject("book", new Book());

            return mav;
        } else {
            // Если пользователь не администратор или не авторизован, перенаправляем его куда-то еще
            return new ModelAndView("redirect:/search");
        }
    }
    @PostMapping("/addBook")
    public ModelAndView addBook(@ModelAttribute Book book) {
        Author existingAuthor = authorService.findAuthorByName(book.getAuthor().getName());
        if (existingAuthor == null) {
            // Если автора нет в базе данных, добавляем его
            existingAuthor = new Author();
            existingAuthor.setName(book.getAuthor().getName());
            authorService.saveAuthor(existingAuthor);
        }

        Language existingLanguage = languageService.findLanguageByName(book.getLanguage().getName());
        if (existingLanguage == null) {
            // Если языка нет в базе данных, добавляем его
            existingLanguage = new Language();
            existingLanguage.setName(book.getLanguage().getName());
            languageService.saveLanguage(existingLanguage);
        }

        Publish existingPublish = publishService.findPublishByName(book.getPublish().getName());
        if (existingPublish == null) {
            // Если языка нет в базе данных, добавляем его
            existingPublish = new Publish();
            existingPublish.setName(book.getPublish().getName());
            publishService.savePublish(existingPublish);
        }

        Cities existingCities = citiesService.findCitiesByName(book.getCities().getName());
        if (existingCities == null) {
            // Если языка нет в базе данных, добавляем его
            existingCities = new Cities();
            existingCities.setName(book.getCities().getName());
            citiesService.saveCities(existingCities);
        }


        book.setAuthor(existingAuthor);
        book.setLanguage(existingLanguage);
        book.setCities(existingCities);
        book.setPublish(existingPublish);


        bookService.saveBook(book);

        return new ModelAndView("redirect:/home");
    }
    @GetMapping("/addCopies")
    public ModelAndView showAddCopiesForm(Model model, @RequestParam Long bookId) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser != null && loggedInUser.getRole().getId() == 1) {
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
        if (book == null ) {
            // Обработка случая, если книга не найдена
            return new ModelAndView("error/404");
        }
        copies.setBook(book);

        StatusCopies freeStatusOpt = statusCopiesRepository.findByStatus("Свободен");
        copies.setStatusCopies(freeStatusOpt);

        copiesRepository.save(copies);

        // Перенаправление на страницу книги с обновленным списком копий
        return new ModelAndView("redirect:/book/" + bookId);
    }

    @PostMapping("/deleteCopie")
    @Transactional
    public ModelAndView deleteCopie(@RequestParam Long copiesId) {
        Optional<Copies> copiesOptional = copiesRepository.findById(copiesId);
        if (copiesOptional.isPresent()) {
            Copies copy = copiesOptional.get();

            // Удаляем связанные записи из orders и order_history
            orderRepository.deleteAllByCopies(copy);
            orderHistoryRepository.deleteAllByCopies(copy);

            // Удаляем экземпляр книги
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

    @GetMapping("/api/authors")
    @ResponseBody
    public List<String> getAuthors(@RequestParam("term") String term) {
        List<Author> authors = authorService.findAuthorsByNameContaining(term);
        List<String> authorNames = new ArrayList<>();
        for (Author author : authors) {
            authorNames.add(author.getName()); // Предполагается, что у объекта Author есть метод getName()
        }
        // Оставляем только первые 4 элемента
        return authorNames.subList(0, Math.min(authorNames.size(), 4));
    }

    @GetMapping("/api/cities")
    @ResponseBody
    public List<String> getCities(@RequestParam("term") String term) {
        List<Cities> cities = citiesService.findCitiesByNameContaining(term);
        List<String> citiesNames = new ArrayList<>();
        for (Cities city : cities) {
            citiesNames.add(city.getName()); // Предполагается, что у объекта Author есть метод getName()
        }
        // Оставляем только первые 4 элемента
        return citiesNames.subList(0, Math.min(citiesNames.size(), 4));
    }

    @GetMapping("/api/publishes")
    @ResponseBody
    public List<String> getPublishes(@RequestParam("term") String term) {
        List<Publish> publishes = publishService.findPublishesByNameContaining(term);
        List<String> publishNames = new ArrayList<>();
        for (Publish publish : publishes) {
            publishNames.add(publish.getName()); // Предполагается, что у объекта Author есть метод getName()
        }
        // Оставляем только первые 4 элемента
        return publishNames.subList(0, Math.min(publishNames.size(), 4));
    }

    @GetMapping("/api/languages")
    @ResponseBody
    public List<String> getLanguages(@RequestParam("term") String term) {
        List<Language> languages = languageService.findLanguagesByNameContaining(term);
        List<String> languagesNames = new ArrayList<>();
        for (Language language : languages) {
            languagesNames.add(language.getName()); // Предполагается, что у объекта Author есть метод getName()
        }
        // Оставляем только первые 4 элемента
        return languagesNames.subList(0, Math.min(languagesNames.size(), 4));
    }
}

