package gr.assignment.book.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableCaching
@EnableScheduling
@Configuration
@Slf4j
public class CacheConfiguration {

    @CacheEvict(cacheNames = "${cache.book.search.name}", allEntries = true)
    @Scheduled(fixedRateString = "${cache.book.search.ttl}")
    public void evictSearchBooksByTitle() {
    }

    @CacheEvict(cacheNames = "${cache.book.name}", allEntries = true)
    @Scheduled(fixedRateString = "${cache.book.ttl}")
    public void evictBookDetails() {
    }

}