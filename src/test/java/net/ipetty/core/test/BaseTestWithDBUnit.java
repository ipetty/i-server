package net.ipetty.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.BeforeClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 带DBUnit功能的基础测试类
 * 
 * @author luocanfeng
 * @date 2014年3月29日
 */
public class BaseTestWithDBUnit extends BaseTest {

	private static final String DBUNIT_PROPERTIES_FILE_PATH = "dbunit/dbunit.properties";

	private static final String DB_DRIVER_CLASS;
	private static final String DB_CONNECTION_URL;
	private static final String DB_USERNAME;
	private static final String DB_PASSWORD;
	private static final String TEST_DATA_FILE; // classpath下的测试数据文件名

	private static final FlatXmlDataSetBuilder DATASET_BUILDER = new FlatXmlDataSetBuilder();
	private static final DefaultDataTypeFactory DATA_TYPE_FACTORY = new MySqlDataTypeFactory();

	static {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			Resource resource = new ClassPathResource(DBUNIT_PROPERTIES_FILE_PATH);
			File file = resource.getFile();
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		DB_DRIVER_CLASS = properties.getProperty("driverClass");
		DB_CONNECTION_URL = properties.getProperty("connectionUrl");
		DB_USERNAME = properties.getProperty("username");
		DB_PASSWORD = properties.getProperty("password");
		TEST_DATA_FILE = properties.getProperty("testDataFile");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cleanInsert();
	}

	protected static void cleanInsert() throws Exception {
		cleanAndInsertTestData(DB_DRIVER_CLASS, DB_CONNECTION_URL, DB_USERNAME, DB_PASSWORD, TEST_DATA_FILE);
	}

	/**
	 * 清除数据库数据,并插入所给测试数据.
	 * 
	 * @param driverClass
	 *            驱动类
	 * @param connectionUrl
	 *            数据库连接url
	 * @param username
	 *            数据库连接帐号
	 * @param password
	 *            数据库连接密码
	 * @param testDataFile
	 *            classpath下的测试数据文件名
	 */
	private static void cleanAndInsertTestData(String driverClass, String connectionUrl, String username,
			String password, String testDataFile) throws Exception {
		final IDatabaseConnection connection = getConnection(driverClass, connectionUrl, username, password);
		final IDataSet data = getDataSet(testDataFile);

		DatabaseConfig dbConfig = connection.getConfig();
		dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, DATA_TYPE_FACTORY);

		try {
			DatabaseOperation.CLEAN_INSERT.execute(connection, data);
		} finally {
			connection.close();
		}
	}

	private static IDataSet getDataSet(String testDataFile) throws DataSetException, IOException {
		final Resource resource = new ClassPathResource(testDataFile);
		return DATASET_BUILDER.build(resource.getFile());
	}

	private static IDatabaseConnection getConnection(String driverClass, String connectionUrl, String username,
			String password) throws ClassNotFoundException, DatabaseUnitException, SQLException {
		Class.forName(driverClass);
		return new DatabaseConnection(DriverManager.getConnection(connectionUrl, username, password));
	}

}
