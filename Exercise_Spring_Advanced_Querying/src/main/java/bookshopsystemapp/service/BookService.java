package bookshopsystemapp.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookService {

    void seedBooks() throws IOException;

    List<String> getAllBooksTitlesAfter();

    Set<String> getAllAuthorsWithBookBefore();

    List<String> getAllTitlesByAgeRestriction(String str);

    List<String> getAllGoldenBooks();

    List<String> getBooksPriceLowerThan5AndGreaterThan40();

    List<String> getBooksNotReleasedInGivenYear(int year);

    List<String> getBooksReleasedBefore(String d1);

    List<String> getBooksByPattern(String str);

    List<String> getBooksByFirstNameOfAuthor(String str);

    Integer countByTitleWithMoreSymbolsThanGivenNumber(Integer symbol);

    List<String> getCountOfCopiesForEachAuthor();

    List<String> reducedBook(String title);

    Integer deletedBooksByCopies(Integer copies);

    Integer countOfUpdatedCopies(String date, Integer copies);


}



