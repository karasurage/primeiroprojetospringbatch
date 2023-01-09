package com.udemy.primeiroprojetospringbatch.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Value("${spring.nome}")
    private String nome;

    @Bean
    public Job job(JobRepository jobRepository,
                   PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository)
//                .start(step(jobRepository, transactionManager))
                .start(imprimeParImparStep(jobRepository, transactionManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }

//    @Bean
//    public Step step(JobRepository jobRepository,
//                     PlatformTransactionManager transactionManager) {
//        return new StepBuilder("step", jobRepository)
//                .tasklet((StepContribution contribution, ChunkContext chunckContext) -> {
//                    System.out.println(String.format("Olá, %s!", nome));
//                    return RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }

    @Bean
    public Step imprimeParImparStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("imprimeParImparStep", jobRepository)
                .<Integer, String>chunk(1, transactionManager)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .writer(imprimeWriter())
                .build();
    }

    public IteratorItemReader<Integer> contaAteDezReader() {
        List<Integer> numeroDeUmAteDez = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new IteratorItemReader<Integer>(numeroDeUmAteDez);
    }

    public FunctionItemProcessor<Integer, String> parOuImparProcessor() {
        return new FunctionItemProcessor<Integer, String>(
                item -> item % 2 == 0 ? String.format("Item %s é Par", item)
                        : String.format("Item %s é Ímpar", item)
        );
    }

    public ItemWriter<String> imprimeWriter() {
        return itens -> itens.forEach(System.out::println);
    }

}