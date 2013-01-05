def testDataDir = new File(".").getCanonicalFile()

println "Reading test suite from ${testDataDir}"
def reader = new TestDefinitionReader(testDataDir)

TestSuite suite = reader.readTestSuite()

println "Running test suite"
TestRunner runner = new TestRunner()
runner.runTestSuite(suite)
