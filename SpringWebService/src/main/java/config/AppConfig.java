package config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import utils.DateHelper;
import utils.IDateHelper;
import utils.IJSonHelper;
import utils.JSonHelper;

@Configuration
@ComponentScan(basePackages = {"model.service", "model.repo", "utils"})
@EnableTransactionManagement
public class AppConfig {
	
	 @Bean
	 public DriverManagerDataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/MyDB?autoReconnect=true&useSSL=false");
		ds.setUsername("root");
		ds.setPassword("12345678x@X");
	    return ds;
	 }
	 
	 @Bean
	 public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
	    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
	    entityManagerFactory.setDataSource(dataSource);
	    entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
	    entityManagerFactory.setPackagesToScan("model.entity");    
	    entityManagerFactory.setJpaPropertyMap(hibernateJpaProperties());
	    return entityManagerFactory;
	 }
	 
	 private Map<String, String> hibernateJpaProperties() {
	    HashMap<String, String> properties = new HashMap<>();
	    properties.put("hibernate.hbm2ddl.auto", "update");
	    properties.put("hibernate.show_sql", "false");
	    properties.put("hibernate.format_sql", "false");
	    return properties;
	 }
	 
     @Bean
     public JpaVendorAdapter jpaVendorAdapter(){
         HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
         adapter.setDatabase(Database.MYSQL);
         adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
         return adapter;
     }
	 
     @Bean
     public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
         return new JpaTransactionManager(entityManagerFactory);
     }
     
     @Bean
     public IJSonHelper getJsonHelper(){
    	 return new JSonHelper();
     }
     
     @Bean
     public IDateHelper getDateHelper(){
    	 return new DateHelper();
     }
     
}
