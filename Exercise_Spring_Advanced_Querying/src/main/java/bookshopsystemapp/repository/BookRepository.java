package bookshopsystemapp.repository;

import bookshopsystemapp.domain.entities.AgeRestriction;
import bookshopsystemapp.domain.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByReleaseDateAfter(LocalDate date);

    List<Book> findAllByReleaseDateBefore(LocalDate date);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByCopiesLessThan(int count);

    List<Book> findAllByPriceLessThan(BigDecimal lowerPrice);

    List<Book> findAllByPriceGreaterThan(BigDecimal lowerPrice);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate d1, LocalDate d2);

    List<Book> findAllByTitleContaining(String str);

    @Query(value = "SELECT b FROM books b  join b.author AS a WHERE a.lastName LIKE :fName ")
    List<Book> findAllBooksByAuthorsLastName(String fName);


    @Query(value = "SELECT count(b) FROM  books b WHERE length (b.title) > :symbols")
    Integer countBooks(Integer symbols);


    @Query(value = "SELECT concat(b.author.firstName, ' ', b.author.lastName) AS name, SUM (b.copies) AS total FROM books b GROUP BY b.author ORDER BY total DESC")
    List<Map<String, String>> findAuthorsByCopies();

    @Query(value = "SELECT b.title, b.editionType, b.ageRestriction , b.price FROM books AS b WHERE b.title = :title")
    Book findOneByTitle(String title);

    @Query(value = "select b.title, b.edition_type, b.age_restriction, b.price from books as b where b.title = :title", nativeQuery = true)
    List<Object[]> reducedBook(@Param(value = "title") String title);


    @Modifying
    @Transactional
    @Query(value = "UPDATE books AS b SET b.copies = b.copies + :copies WHERE b.releaseDate > :date")
    Integer increaseBookCopies(LocalDate date, Integer copies);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM books b WHERE b.copies < :copies")
    Integer deleteBooksByCopiesLessThan(Integer copies);

    @Query(value = "call books_by_author(:firstName, :lastName)", nativeQuery = true)
    Object[] storedProcedure(@Param(value = "firstName") String firstName, @Param(value = "lastName") String lastName);
}
