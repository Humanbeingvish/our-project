package com.cognicx.AppointmentRemainder.constant;

public class ApplicationConstant {

  public static final String SPRING_BASE_CLASS_PROPERTYSOURCE = "classpath:application.properties";
  public static final String LOGGER_PROPERTYSOURCE = "classpath:log4j.properties";
  // Hibernate Configuration
  public static final String HIBERNATE_DIALECT = "hibernate.dialect";
  public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
  public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

  /* Data Source Names */
  public static final String FIRST_DATA_SOURCE_BEAN_NAME = "firstDataSource";
  public static final String FIRST_ENTITY_MANAGER = "firstEntityManager";
  public static final String FIRST_TRANSACTION_MANAGER = "firstTransactionManager";
  public static final String FIRST_JDBC_TEMPLATE = "firstJdbcTemplate";
	public static final String FIRST_MODAL_PACKAGE = "com.cognicx.AppointmentRemainder.model";
  public static final String FIRST_PERSISTENCE_UNIT_NAME = "first";
  public static final String WILDFLY_STR = "WildFly";
  
  
	public static final String COMPONENT_SCAN = "com.cognicx";
	public static final String ENTITY_SCAN = "com.cognicx.AppointmentRemainder.model";

}
