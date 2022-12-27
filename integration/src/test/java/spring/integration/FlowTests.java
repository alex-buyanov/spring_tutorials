package spring.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.rometools.rome.feed.synd.SyndEntryImpl;

//Annotation disables automatic start of inbound-channel-adapter (see integration.xml)
//and also redirects output of outbound-channel-adapter into a different file (see the same XML)
@SpringBootTest({"auto.startup=false", "feed.file.name=Test"})
class FlowTests {
    private static final Logger LOG = LoggerFactory.getLogger(FlowTests.class);

    @Autowired
    private SourcePollingChannelAdapter newsAdapter;

    @Autowired
    private MessageChannel news;

    @Test
    void test() throws Exception {
        LOG.info("Starting the test...");
        LOG.info("Checking that adapter is not running...");
        assertThat(this.newsAdapter.isRunning()).isFalse();
        SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setTitle("Test Title");
        syndEntry.setLink("http://characters/frodo");
        File out = new File("/tmp/si/Test");
        if (out.exists()) {
            assertThat(out.delete()).isTrue();
        }
        LOG.info("Checking that output file does not exist...");
        assertThat(out).doesNotExist();
        this.news.send(MessageBuilder.withPayload(syndEntry).build());
        assertThat(out).exists();
        BufferedReader br = new BufferedReader(new FileReader(out));
        String line = br.readLine();
        assertThat(line).isEqualTo("Test Title @ http://characters/frodo");
        br.close();
        if (out.exists()) {
            assertThat(out.delete()).isTrue();
        }
    }

}