def testDataDir = new File(".").getCanonicalFile()
println "Reading test config in " + testDataDir


def reader = new TestDefinitionReader(testDataDir)

Collection<TestCase> suite = reader.readTestSuite()


TestRunner runner = new TestRunner()
runner.runTestSuite(suite)
