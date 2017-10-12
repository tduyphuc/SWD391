package phuc.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import phuc.utils.IJSonHelper;
import phuc.utils.JSonHelper;

@Configuration
@ComponentScan(basePackages = "phuc")
//@EnableJpaRepositories(basePackages = "phuc.entity")
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
//	    entityManagerFactory.setPersistenceUnitName("hibernate-persistence");
	    entityManagerFactory.setDataSource(dataSource);
	    entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
	    //entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
	    entityManagerFactory.setPackagesToScan("phuc.entity");    
	    entityManagerFactory.setJpaPropertyMap(hibernateJpaProperties());
	    return entityManagerFactory;
	 }
	 
	 private Map<String, String> hibernateJpaProperties() {
	    HashMap<String, String> properties = new HashMap<>();
	    properties.put("hibernate.hbm2ddl.auto", "update");
	    properties.put("hibernate.show_sql", "false");
	    properties.put("hibernate.format_sql", "false");
//	    properties.put("hibernate.hbm2ddl.import_files", "insert-data.sql");
//	    properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");	    
//	    properties.put("hibernate.c3p0.min_size", "2");
//	    properties.put("hibernate.c3p0.max_size", "5");
//	    properties.put("hibernate.c3p0.timeout", "300"); // 5mins 
	    return properties;
	 }
	 
     @Bean
     public JpaVendorAdapter jpaVendorAdapter(){
         HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
         adapter.setDatabase(Database.MYSQL);
         //adapter.setShowSql(true);
         //adapter.setGenerateDdl(true);
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
}
