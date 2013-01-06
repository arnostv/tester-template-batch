import spock.lang.Specification

class FileGroupSpec extends Specification{

    def "should find file by type"() {
        setup:
        def group = new FileGroup(prefix:  "group1", filesInGroup:[new File("group1.params.groovy"), new File("group1.xml")])

        when:
        def xmlFile = group.fileByType(".xml")
        def groovyParamsFile = group.fileByType(".params.groovy")
        def txtFile = group.fileByType(".txt")

        then:
        xmlFile == new File("group1.xml")
        groovyParamsFile == new File("group1.params.groovy")
        txtFile == null
    }

    def "should derive path from base directory"() {
        setup:
        def base = new File("/my/path/basefile")
        def file1 = new File(base, "suite1/test1/file1.xml")

        when:
        def group = new FileGroup(prefix: "file1",filesInGroup: [file1])

        then:
        group.pathFromBase(base) == "suite1/test1"
    }

}
