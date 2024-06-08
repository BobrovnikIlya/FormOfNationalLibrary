package com.example.formofnationallibrary.Controller;

import com.example.formofnationallibrary.Entities.*;

import com.example.formofnationallibrary.Service.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;


@RestController
@SessionAttributes("loggedInUser")
public class ExportController {

    @Autowired
    BookService bookService;
    @Autowired
    OrderHistoryService orderHistoryService;
    @Autowired
    OrderService orderService;
    @Autowired
    QueueService queueService;
    @Autowired
    UserService userService;
    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/exportFormular")
    public ResponseEntity<byte[]> exportFormular(Model model) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.badRequest().build();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            PdfFont font = PdfFontFactory.createFont("fonts/arial.ttf", PdfEncodings.IDENTITY_H);
            document.setFont(font);

            addUserDetails(document, loggedInUser);
            addFavorites(document, loggedInUser);
            addOrders(document, loggedInUser);
            addOrderHistory(document, loggedInUser);
            addQueue(document, loggedInUser);

            document.close();
            pdfDocument.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=formular.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private void addUserDetails(Document document, User user) {
        Optional<User> userOptional = userService.findById(user.getId());
        if (userOptional.isPresent()) {
            User userDetails = userOptional.get();
            document.add(new Paragraph("Профиль пользователя").setTextAlignment(TextAlignment.CENTER));

            float[] columnWidths = {1, 2};
            Table table = createCenteredTable(columnWidths);

            addTableHeader(table, new String[]{"Поле", "Значение"});
            addRow(table, "Логин", userDetails.getLogin());
            addRow(table, "Электронная почта", userDetails.getEmail());
            addRow(table, "ФИО", userDetails.getFIO());
            addRow(table, "Дата рождения", userDetails.getDate().toString());
            addRow(table, "Номер телефона", userDetails.getPhoneNumber());
            addRow(table, "Номер документа", userDetails.getNumberDocument());
            addRow(table, "Роль", userDetails.getRole().getName());
            addRow(table, "Описание", userDetails.getDescription());

            document.add(table);
        }
    }

    private void addFavorites(Document document, User user) {
        List<Favorite> favorites = favoriteService.findByUserId(user.getId());
        if (!favorites.isEmpty()) {
            document.add(new Paragraph("Избранные книги").setTextAlignment(TextAlignment.CENTER));

            float[] columnWidths = {2, 3};
            Table table = createCenteredTable(columnWidths);

            addTableHeader(table, new String[]{"Автор","Название книги"});

            for (Favorite favorite : favorites) {
                Book book = bookService.findById(favorite.getBookId());
                addRow(table, book != null ? book.getAuthor().getName() : "Неизвестно", book.getName());
            }
            document.add(table);
        }
    }
    private void addOrders(Document document, User user) {
        List<Order> orders = orderService.findByUserId(user.getId());
        if (!orders.isEmpty()) {
            document.add(new Paragraph("Заказы").setTextAlignment(TextAlignment.CENTER));

            float[] columnWidths = {2, 1, 2, 2};
            Table table = createCenteredTable(columnWidths);

            addTableHeader(table, new String[]{"Название книги", "Шифр экземпляра", "Дата выдачи", "Дата возврата"});

            for (Order order : orders) {
                Book book = order.getCopies().getBook();
                addRow(table, book != null ? book.getName() : "Неизвестно", order.getCopies().getCipher(), order.getIssueDate().toString(), order.getReturnDate().toString());
            }

            document.add(table);
        }
    }

    private void addOrderHistory(Document document, User user) {
        List<OrderHistory> orderHistoryList = orderHistoryService.findByUserId(user.getId());
        if (!orderHistoryList.isEmpty()) {
            document.add(new Paragraph("История заказов").setTextAlignment(TextAlignment.CENTER));

            float[] columnWidths = {2, 1, 2, 2};
            Table table = createCenteredTable(columnWidths);

            addTableHeader(table, new String[]{"Название книги", "Шифр экземпляра", "Дата выдачи", "Дата возврата"});

            for (OrderHistory orderHistory : orderHistoryList) {
                Book book = bookService.findById(orderHistory.getCopies().getBook().getId());
                addRow(table, book != null ? book.getName() : "Неизвестно", orderHistory.getCopies().getCipher(), orderHistory.getIssueDate().toString(), orderHistory.getReturnDate().toString());
            }

            document.add(table);
        }
    }

    private void addQueue(Document document, User user) {
        List<Queue> queue = queueService.findByUserId(user.getId());
        if (!queue.isEmpty()) {
            document.add(new Paragraph("Очередь").setTextAlignment(TextAlignment.CENTER));

            float[] columnWidths = {2, 1};
            Table table = createCenteredTable(columnWidths);

            addTableHeader(table, new String[]{"Название книги", "Номер в очереди"});

            for (Queue queueItem : queue) {
                Book book = bookService.findById(queueItem.getBook().getId());
                addRow(table, book != null ? book.getName() : "Неизвестно", String.valueOf(queueItem.getQueueNumber()));
            }

            document.add(table);
        }
    }

    private Table createCenteredTable(float[] columnWidths) {
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(90));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return table;
    }

    private void addTableHeader(Table table, String[] headers) {
        for (String header : headers) {
            Cell headerCell = new Cell();
            headerCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            headerCell.add(new Paragraph(header));
            table.addCell(headerCell);
        }
    }

    private void addRow(Table table, String... values) {
        for (String value : values) {
            table.addCell(new Cell().add(new Paragraph(value)));
        }
    }
}
