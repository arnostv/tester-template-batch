
@groovy.transform.Immutable class FileGroup {

    String prefix
    Collection<File> filesInGroup

    def fileByType(String type) {
        def fileName = prefix + type
        filesInGroup.find {it.name == fileName}
    }

    def directory() {
        filesInGroup[0].parentFile
    }

    @Override
    public String toString() {
        return "FileGroup ${prefix} ${filesInGroup.size()}:${filesInGroup}"
    }

}
