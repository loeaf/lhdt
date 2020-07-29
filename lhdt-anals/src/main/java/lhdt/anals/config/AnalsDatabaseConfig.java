/**
 * 
 */
package lhdt.anals.config;

import javax.sql.DataSource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

/**
 * db connection
 * @author gravity@daumsoft.com
 *
 */
@Slf4j
@Configuration
@MapperScan(value="lhdt.anals", annotationClass = AnalsConnMapper.class, sqlSessionFactoryRef = "analsSqlSessionFactory")
@EnableTransactionManagement
public class AnalsDatabaseConfig {
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("lhdt.anals");
		factory.setDataSource(analsDataSource());
		return factory;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	 
	    return transactionManager;
	}
	 
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	
	/**
	 * TODO  각  접속정보 암호화 필요
	 * @return
	 */
	@Bean(name="analsDataSource")
	@Primary
	@ConfigurationProperties(prefix="spring.anals.datasource")
	public DataSource analsDataSource() {
		DriverManagerDataSource dmds = DataSourceBuilder.create().type(DriverManagerDataSource.class).build();
		
		//
		log.info("<< {}", ToStringBuilder.reflectionToString(dmds));
		return dmds;
	}
	
	@Bean(name = "analsSqlSessionFactory")
	@Primary
	public SqlSessionFactory analsSqlSessionFactory(@Qualifier("analsDataSource") DataSource analsDataSource, ApplicationContext context) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(analsDataSource);
		
		//
		sqlSessionFactoryBean.setMapperLocations(context.getResources("classpath*:mybatis/**/*.xml"));
		
		//
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		
		//
		log.info("<< {}", ToStringBuilder.reflectionToString(sqlSessionFactory));
		return sqlSessionFactory;
	}
	
	@Bean(name = "analsSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate analsSqlSessionTemplate(SqlSessionFactory analsSqlSessionFactory) throws Exception{
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(analsSqlSessionFactory);
		
		//
		log.info("<< {}", ToStringBuilder.reflectionToString(sqlSessionTemplate));
		return sqlSessionTemplate;
	}
}
