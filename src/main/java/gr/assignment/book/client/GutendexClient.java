package gr.assignment.book.client;

import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.BookListDto;
import gr.assignment.book.exception.GutendexClientException;
import gr.assignment.book.exception.GutendexClientHttpErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GutendexClient {

    private static final String BOOKS_PATH = "/books";

    private final RestTemplate restTemplate;
    @Value("${gutendex.api.url}")
    private String apiUrl;

    public List<BookDto> searchBooks(String title, Integer page, Integer pageSize) throws GutendexClientException {
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl + BOOKS_PATH)
                    .queryParam("search", title);

            if (page != null) {
                uriBuilder.queryParam("page", page);
            }

            if (pageSize != null) {
                uriBuilder.queryParam("pageSize", pageSize);
            }

            UriComponents uriComponents = uriBuilder.build(false);

            ResponseEntity<BookListDto> response = restTemplate.getForEntity(uriComponents.toUriString(), BookListDto.class);
            BookListDto bookListDto = response.getBody();

            if (bookListDto != null && bookListDto.getResults() != null) {
                return bookListDto.getResults();
            } else {
                return Collections.emptyList(); // Return an empty list if the response is null or the results are null
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error received for title: {} with page: \"{}\" and pageSize: {}", title, page, pageSize, e);
            throw new GutendexClientHttpErrorException(e);
        } catch (HttpStatusCodeException e) {
            log.error("Status code occurred while searching book with title: \"{}\" ", title, e);
            throw new GutendexClientHttpErrorException(e);
        } catch (RestClientException e) {
            throw new GutendexClientException(e);
        }
    }

    public List<BookDto> getAll() {
        String finalUrl = apiUrl + BOOKS_PATH;
        try {
            ResponseEntity<BookListDto> response = restTemplate.getForEntity(finalUrl, BookListDto.class);
            BookListDto bookListDto = response.getBody();

            if (bookListDto != null && bookListDto.getResults() != null) {
                return bookListDto.getResults();
            } else {
                return Collections.emptyList(); // Return an empty list if the response is null or the results are null
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error received while retrieve all books", e);
            throw new GutendexClientHttpErrorException(e);
        } catch (HttpStatusCodeException e) {
            log.error("Status code error occurred while retrieve all books", e);
            throw new GutendexClientHttpErrorException(e);
        } catch (RestClientException e) {
            throw new GutendexClientException(e);
        }
    }

    public BookDto getBookById(Long bookId) {
        String finalUrl = apiUrl + BOOKS_PATH + "/" + bookId;
        try {
            ResponseEntity<BookDto> response = restTemplate.getForEntity(
                    finalUrl,
                    BookDto.class,
                    bookId);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Client error received for book with id: {}", bookId, e);
            throw new GutendexClientHttpErrorException(e);
        } catch (HttpStatusCodeException e) {
            log.error("Status code error occurred while retrieve book with id: {} ", bookId, e);
            throw new GutendexClientHttpErrorException(e);
        } catch (RestClientException e) {
            throw new GutendexClientException(e);
        }
    }

}