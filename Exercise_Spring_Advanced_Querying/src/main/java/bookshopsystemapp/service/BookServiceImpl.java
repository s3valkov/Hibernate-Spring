package bookshopsystemapp.service;

import bookshopsystemapp.domain.entities.*;
import bookshopsystemapp.repository.AuthorRepository;
import bookshopsystemapp.repository.BookRepository;
import bookshopsystemapp.repository.CategoryRepository;
import bookshopsystemapp.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final static String BOOKS_FILE_PATH = "C:\\Users\\Stefan\\IdeaProjects\\04.JavaHibernate\\Exercise_Spring_Advanced_Querying\\src\\main\\resources\\files\\books.txt";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] booksFileContent = this.fileUtil.getFileContent(BOOKS_FILE_PATH);
        for (String line : booksFileContent) {
            String[] lineParams = line.split("\\s+");

            Book book = new Book();
            book.setAuthor(this.getRandomAuthor());

            EditionType editionType = EditionType.values()[Integer.parseInt(lineParams[0])];
            book.setEditionType(editionType);

            LocalDate releaseDate = LocalDate.parse(lineParams[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
            book.setReleaseDate(releaseDate);

            int copies = Integer.parseInt(lineParams[2]);
            book.setCopies(copies);

            BigDecimal price = new BigDecimal(lineParams[3]);
            book.setPrice(price);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(lineParams[4])];
            book.setAgeRestriction(ageRestriction);

            StringBuilder title = new StringBuilder();
            for (int i = 5; i < lineParams.length; i++) {
                title.append(lineParams[i]).append(" ");
            }

            book.setTitle(title.toString().trim());

            Set<Category> categories = this.getRandomCategories();
            book.setCategories(categories);

            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<String> getAllBooksTitlesAfter() {
        List<Book> books = this.bookRepository.findAllByReleaseDateAfter(LocalDate.parse("2000-12-31"));

        return books.stream().map(b -> b.getTitle()).collect(Collectors.toList());
    }

    @Override
    public Set<String> getAllAuthorsWithBookBefore() {
        List<Book> books = this.bookRepository.findAllByReleaseDateBefore(LocalDate.parse("1990-01-01"));

        return books.stream().map(b -> String.format("%s %s", b.getAuthor().getFirstName(), b.getAuthor().getLastName())).collect(Collectors.toSet());
    }

    private Author getRandomAuthor() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.authorRepository.count() - 1)) + 1;

        return this.authorRepository.findById(randomId).orElse(null);
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new LinkedHashSet<>();

        Random random = new Random();
        int length = random.nextInt(5);

        for (int i = 0; i < length; i++) {
            Category category = this.getRandomCategory();

            categories.add(category);
        }

        return categories;
    }

    private Category getRandomCategory() {
        Random random = new Random();

        int randomId = random.nextInt((int) (this.categoryRepository.count() - 1)) + 1;

        return this.categoryRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<String> getAllTitlesByAgeRestriction(String str) {
        return this.bookRepository.findAllByAgeRestriction(AgeRestriction.valueOf(str.toUpperCase()))
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllGoldenBooks() {
        return this.bookRepository.findAllByCopiesLessThan(5000)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    public List<String> getBooksPriceLowerThan5AndGreaterThan40() {
        List<String> result = this.bookRepository.findAllByPriceLessThan(BigDecimal.valueOf(5))
                .stream()
                .map(b -> String.format("%s - %s"
                        , b.getTitle()
                        , b.getPrice()))
                .collect(Collectors.toList());

        result.addAll(this.bookRepository.findAllByPriceGreaterThan(BigDecimal.valueOf(40))
                .stream()
                .map(b -> String.format("%s - %s"
                        , b.getTitle()
                        , b.getPrice()))
                .collect(Collectors.toList()));

        return result;
    }

    @Override
    public List<String> getBooksNotReleasedInGivenYear(int year) {
        LocalDate d1 = LocalDate.parse(year + "-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate d2 = LocalDate.parse(year + "-12-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        return this.bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(d1, d2)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBooksReleasedBefore(String d1) {
        LocalDate date = LocalDate.parse(d1, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return this.bookRepository.findAllByReleaseDateBefore(date)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBooksByPattern(String str) {

        return this.bookRepository.findAllByTitleContaining(str.toLowerCase())
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBooksByFirstNameOfAuthor(String str) {

        List<Book> books = this.bookRepository.findAllBooksByAuthorsLastName(str + "%");

        return books.stream().map(book -> String.format("%s (%s %s)",
                book.getTitle(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName())).collect(Collectors.toList());
    }

    @Override
    public Integer countByTitleWithMoreSymbolsThanGivenNumber(Integer symbol) {
        return this.bookRepository.countBooks(symbol);
    }

    @Override
    public List<String> getCountOfCopiesForEachAuthor() {
        List<String> results = new ArrayList<>();
        this.bookRepository.findAuthorsByCopies().forEach(map ->
                results.add(String.format("%s - %s", map.get("name"), map.get("total"))));

        return results;

    }

    @Override
    public List<String> reducedBook(String title) {
        List<String> result = new ArrayList<String>();
        for (Object[] objects : this.bookRepository.reducedBook(title)) {
            result.add(String.format("%s %s %s %s", objects[0], EditionType.values()[Integer.parseInt(objects[1].toString())], AgeRestriction.values()[Integer.parseInt(objects[2].toString())], objects[3]));
        }

        return result;

    }

    @Override
    public Integer deletedBooksByCopies(Integer copies) {
        return this.bookRepository.deleteBooksByCopiesLessThan(copies);
    }

    @Override
    public Integer countOfUpdatedCopies(String date, Integer copies) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return this.bookRepository.increaseBookCopies(LocalDate.parse(date, dateTimeFormatter), copies);
    }


}
