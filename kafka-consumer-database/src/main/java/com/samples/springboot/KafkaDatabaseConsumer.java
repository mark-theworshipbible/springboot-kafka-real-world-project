package com.samples.springboot;

import com.samples.springboot.entity.WikimediaData;
import com.samples.springboot.repository.WikimediaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaDatabaseConsumer {
    private final WikimediaDataRepository repository;

    public KafkaDatabaseConsumer(final WikimediaDataRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "wikimedia_recentchange",
            groupId = "myGroup"
    )
    public void consume(String eventMessage) {
        log.info(String.format("Event message received -> %s", eventMessage));

        WikimediaData wikimediaData = new WikimediaData();
        wikimediaData.setWikiEventData(eventMessage);

        repository.save(wikimediaData);
    }
}
