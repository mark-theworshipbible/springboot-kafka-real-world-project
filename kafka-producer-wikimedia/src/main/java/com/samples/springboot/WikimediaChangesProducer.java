package com.samples.springboot;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WikimediaChangesProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic = "wikimedia_recentchange";

        // to read real-time stream data from widimedia, we use event source
        BackgroundEventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        BackgroundEventSource eventSource = new BackgroundEventSource.Builder(eventHandler,
                new EventSource.Builder(
                        ConnectStrategy.http(URI.create(url))
                                .connectTimeout(10, TimeUnit.SECONDS)
                        // connectTimeout and other HTTP options are now set through
                        // HttpConnectStrategy
                )
        )
                .threadPriority(Thread.MAX_PRIORITY)
                // threadPriority, and other options related to worker threads,
                // are now properties of BackgroundEventSource
                .build();
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);
    }
}
