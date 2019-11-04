package bookshopsystemapp.controller;

import bookshopsystemapp.service.AuthorService;
import bookshopsystemapp.service.BookService;
import bookshopsystemapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class BookshopController implements CommandLineRunner {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookService bookService;
    private final Scanner scanner;

    @Autowired
    public BookshopController(AuthorService authorService, CategoryService categoryService, BookService bookService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... strings) throws Exception {
        this.authorService.seedAuthors();
        this.categoryService.seedCategories();
        this.bookService.seedBooks();

        // Problem 01
//        String str = scanner.nextLine();
        // this.bookService.getAllTitlesByAgeRestriction(str).forEach(System.out::println);

        // Problem 02
        //this.bookService.getAllGoldenBooks().forEach(System.out::println);

        //Problem 03
        //this.bookService.getBooksPriceLowerThan5AndGreaterThan40().forEach(System.out::println);

        //Problem 04
        //  int year = Integer.parseInt(scanner.nextLine());
        //this.bookService.getBooksNotReleasedInGivenYear(year).forEach(System.out::println);


        //Problem 05

        //this.bookService.getBooksReleasedBefore(str).forEach(System.out::println);

        //Problem 06
        //this.authorService.authorsEndingWith(str).forEach(System.out::println);


        //Problem 07
        //this.bookService.getBooksByPattern(str).forEach(System.out::println);

        //Problem 08
        //this.bookService.getBooksByFirstNameOfAuthor(str).forEach(System.out::println);

        //Problem 09
        System.out.println(this.bookService.countByTitleWithMoreSymbolsThanGivenNumber(12));

        //Problem 10
        //this.bookService.getCountOfCopiesForEachAuthor().forEach(System.out::println);

        //Problem 11
        this.bookService.reducedBook("Thrones").forEach(System.out::println);



        //Problem 13
        System.out.println(this.bookService.deletedBooksByCopies(100));
    }
}
