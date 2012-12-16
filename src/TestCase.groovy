@groovy.transform.Immutable class TestCase {

    String location
    String paramsScriptPath
    String actionScriptPath

    Collection<TestStep> readSteps() {
        File locationDir = new File(location)
        assert locationDir.isDirectory()

        def allFiles = locationDir.listFiles()
        def files = allFiles.findAll {
            it.isFile() && it.getName().endsWith(".xml")
        }

        files.collect{
            def path = it.getCanonicalPath()
            def groovyScript = path.replaceAll("\\.xml", ".groovy")
            def groovyScriptPath = null
            if (new File(groovyScript).isFile()) {
                println "Found action script for step $groovyScript"
                groovyScriptPath = groovyScript
            }
            new TestStep(templateFilePath: path, actionScriptPath: groovyScriptPath)
        }
    }

}
