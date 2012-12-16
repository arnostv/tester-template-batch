@groovy.transform.Immutable class TestCase {

    String location
    String paramsScriptPath
    String actionScriptPath

    Collection<TestStep> readSteps() {
        File locationDir = new File(location)
        assert locationDir.isDirectory()

        def files = locationDir.listFiles().findAll {
            it.isFile() && it.getName().endsWith(".xml")
        }

        files.collect{ new TestStep(templateFilePath: it.getCanonicalPath())}
    }

}
