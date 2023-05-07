package gr.assignment.book.client;

import gr.assignment.book.dto.BookDto;
import gr.assignment.book.dto.BookListDto;
import gr.assignment.book.exception.GutendexClientException;
import gr.assignment.book.exception.GutendexClientHttpErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GutendexClientTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    GutendexClient gutendexClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(gutendexClient, "apiUrl", "http://example.com/api");
    }

    @Test
    void searchBooks_shouldReturnBookListDto() {
        // given
        String title = "Sample title";
        int page = 1;
        int pageSize = 10;

        BookListDto expectedBookListDto = new BookListDto();

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");

        expectedBookListDto.setResults(List.of(bookDto1, bookDto2));


        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(ResponseEntity.ok(expectedBookListDto));

        // when
        List<BookDto> response = gutendexClient.searchBooks(title, page, pageSize);

        // then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1, response.get(0).getId());
        assertEquals("Book 1", response.get(0).getTitle());
        assertEquals(2, response.get(1).getId());
        assertEquals("Book 2", response.get(1).getTitle());

        verify(restTemplate, times(1)).getForEntity(anyString(), any());
    }

    @Test
    void searchBooks_shouldThrowGutendexClientException_whenRestClientExceptionOccurs() {
        // given
        String title = "Sample title";
        int page = 1;
        int pageSize = 10;

        when(restTemplate.getForEntity(anyString(), any()))
                .thenThrow(new RestClientException("Internal Server Error"));

        // when
        assertThrows(GutendexClientException.class,
                () -> gutendexClient.searchBooks(title, page, pageSize));


        verify(restTemplate, times(1)).getForEntity(anyString(), any());
    }

    @Test
    void getBookById_shouldReturnBookDto() {
        // given
        long bookId = 1L;

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(bookId);
        expectedBookDto.setTitle("Book 1");

        when(restTemplate.getForEntity(anyString(), any(), anyLong()))
                .thenReturn(ResponseEntity.ok(expectedBookDto));

        // when
        BookDto result = gutendexClient.getBookById(bookId);

        // then
        assertEquals(bookId, result.getId());
        assertEquals("Book 1", result.getTitle());

        verify(restTemplate, times(1)).getForEntity(anyString(), any(), anyLong());
    }

    @Test
    void getBookById_shouldThrowNoBooksFoundException_whenHttpClientErrorExceptionNotFoundOccurs() {
        // given
        long bookId = 1;

        when(restTemplate.getForEntity(anyString(), any(), anyLong()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

         assertThrows(GutendexClientHttpErrorException.class,
                () -> gutendexClient.getBookById(bookId));

        verify(restTemplate, times(1)).getForEntity(anyString(), any(), anyLong());
    }

    @Test
    void getBookById_shouldThrowGutendexClientException_whenRestClientExceptionOccures() {
        // given
        long bookId = 1;

        when(restTemplate.getForEntity(anyString(), any(), anyLong()))
                .thenThrow(RestClientException.class);

        // when
        assertThrows(GutendexClientException.class,
                () -> gutendexClient.getBookById(bookId));

        // then
        verify(restTemplate, times(1)).getForEntity(anyString(), any(), anyLong());
    }

}
