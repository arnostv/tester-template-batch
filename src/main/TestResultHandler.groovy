class TestResultHandler {
    static String TEST_OUTPUT_DIRECTORY = "testoutput"

    def handleTestResult(TestStepData testStepData, def result) {
        if (result!=null) {
            File testSuitePath = new File(testStepData.testSTS.suite.locationPath)
            File testOutputDirectory = findTestOutputDirectory(testSuitePath);
            File testResultDir = new File(testOutputDirectory, testStepData.testSTS.step.fileGroup.pathFromBase(testSuitePath))

            //println "Result is ${result}"
            println "Result base dir is ${testOutputDirectory} --> output to ${testResultDir}"
            def testName = testStepData.testSTS.step.fileGroup.prefix
            //println "Result for test ${testName}"

            if (!testResultDir.isDirectory()) {
                def directoryCreated = testResultDir.mkdirs()
                if (directoryCreated) {
                    println "Created ${testResultDir}"
                } else {
                    println "Failed to create ${testResultDir}"
                }
            }

            File testResultFile = new File(testResultDir, testName + ".txt")
            testResultFile.write(result.toString())

        } else {
            println "Undefined result"
        }
    }

    File findTestOutputDirectory(File testSuite) {
        def testOutput = new File(testSuite.parentFile, TEST_OUTPUT_DIRECTORY)

        assert testOutput.isDirectory()
        assert testOutput.canWrite()
        testOutput
    }
}
