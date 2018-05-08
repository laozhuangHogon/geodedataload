package io.pivotal.cso.loaddata;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class DataLoadBatchApplication {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DataLoadBatchApplication.class);
	}

	public static void main(String[] args) throws Exception {
		ClientCache cache = new ClientCacheFactory().set("name", "ClientWorker").set("cache-xml-file", "cacheClient.xml")
				.create();
		SpringApplication.run(DataLoadBatchApplication.class, args);

//		try {
//			System.exit(SpringApplication.exit(SpringApplication.run(DataLoadBatchApplication.class, args)));
//		} catch (Exception ex) {
//			System.out.println("ERROR");
//			System.exit(1);
//		}
	}

}
