package io.pivotal.cso.loaddata;



import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.GemfireItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@EnableBatchProcessing
public class BatchConfigurationHistoryRate {

    @Autowired
    public DataSource dataSource;

    private static final String QUERY_FIND_HISTORYRATE = "select * from history_rate";
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Value("${file.input.csvName.historyRate}")
    private String inputFileName;

    @Value("${attributes.list.historyRate}")
    private String[] attributesList;

    @Value("${spring.batch.chunk.control}")
    private Integer chunkControl;

    // tag::readerwriterprocessor[]

    //to get obj from file
    @Bean
    public FlatFileItemReader<HistoryRate> readerHistoryRate() {
        FlatFileItemReader<HistoryRate> reader = new FlatFileItemReader<HistoryRate>();

        reader.setResource(new ClassPathResource(inputFileName));
        // skip csv header
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<HistoryRate>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(attributesList);
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<HistoryRate>() {
                    {
                        setTargetType(HistoryRate.class);
                    }
                });
            }
        });
        return reader;
    }

    // to get obj from db
    @Bean
    public ItemReader<HistoryRate> jdbcPagingItemReaderHistoryRate() {
        JdbcCursorItemReader databaseReader = new JdbcCursorItemReader();
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_HISTORYRATE);

        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(HistoryRate.class));
        return databaseReader;
    }


    @Bean
    public GemfireTemplate historyGemFireTemplate() {
        Region historyRegion = CacheFactory.getAnyInstance().getRegion("historyRate");
        GemfireTemplate historyGemFireTemplate = new GemfireTemplate();
        historyGemFireTemplate.setRegion(historyRegion);
        return historyGemFireTemplate;
    }


    @Bean
    public GemfireItemWriter<String, HistoryRate> writerHistoryRate() {
        GemfireItemWriter<String, HistoryRate> writer = new GemfireItemWriter<String, HistoryRate>();
        writer.setTemplate(this.historyGemFireTemplate());
        writer.setItemKeyMapper(new Converter<HistoryRate, String>() {
            public String convert(HistoryRate historyRate) {
                return "key" + UUID.randomUUID();
            }
        });
        return writer;
    }


    @Bean
    public Job jobImportHistoryRate() {
        return jobBuilderFactory.get("jobImportHistoryRate").incrementer(new RunIdIncrementer())
                .flow(stepImportHistoryRate()).end().build();
    }

    @Bean
    public Step stepImportHistoryRate() {
//        return stepBuilderFactory.get("stepImportHistoryRate").<HistoryRate, HistoryRate>chunk(chunkControl).reader(readerHistoryRate())
//                .writer(writerHistoryRate()).build();

        return stepBuilderFactory.get("stepImportHistoryRate").<HistoryRate, HistoryRate>chunk(chunkControl).reader(jdbcPagingItemReaderHistoryRate())
                .writer(writerHistoryRate()).build();
    }
    // end::jobstep[]
}
