@Grab(group='org.springframework', module='spring', version='2.5.6')
import org.springframework.jdbc.core.JdbcTemplate

@GrabConfig(systemClassLoader=true)
@Grab(group='hsqldb', module='hsqldb', version='1.8.0.10')
import org.hsqldb.jdbcDriver

println "Default test suite action for test - uppercase it -- run ID ${test_run_id}"

def driverClass = Class.forName("org.hsqldb.jdbcDriver")

def utils = new TestingUtils()
utils.hello()

println payload.toString().toUpperCase()

