@groovy.transform.Immutable class TestCase {

    String location

    Collection<TestStep> readSteps() {
        File locationDir = new File(location)
        assert locationDir.isDirectory()

        def files = locationDir.listFiles().findAll {
            it.isFile() && it.getName().endsWith(".xml")
        }

        files.collect{ new TestStep(location: it.getCanonicalPath())}
    }

}
