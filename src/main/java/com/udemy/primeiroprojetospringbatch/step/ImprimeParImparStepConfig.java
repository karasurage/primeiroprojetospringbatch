package com.udemy.primeiroprojetospringbatch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImprimeParImparStepConfig {

    @Bean
    public Step imprimeParImparStep(JobRepository jobRepository,
                                    ItemReader<Integer> contaAteDezReader,
                                    ItemProcessor<Integer,
                                    String> parOuImparProcessor,
                                    ItemWriter<String> imprimeWriter,
                                    PlatformTransactionManager transactionManager) {

        return new StepBuilder("imprimeParImparStep", jobRepository)
                .<Integer, String>chunk(1, transactionManager)
                .reader(contaAteDezReader)
                .processor(parOuImparProcessor)
                .writer(imprimeWriter)
                .build();
    }
}
