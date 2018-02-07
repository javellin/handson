package br.com.azi.handson.job.olamundo.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OlaMundoProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        log.info("Estou processando {}", item);
        return item.concat(" processado.");
    }
}
