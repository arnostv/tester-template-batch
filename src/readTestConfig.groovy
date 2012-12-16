def testDataDir = new File(".").getCanonicalFile()
println "Reading test config in " + testDataDir


def reader = new TestDefinitionReader(testDataDir)

TestSuite suite = reader.readTestSuite()


TestRunner runner = new TestRunner()
runner.runTestSuite(suite)
