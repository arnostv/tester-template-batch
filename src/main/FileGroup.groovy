
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

    def pathFromBase(File providedBasePath) {
        if (directory().absolutePath.startsWith(providedBasePath.absolutePath)) {
            return directory().absolutePath.substring(1+providedBasePath.absolutePath.length())
        }
    }


    @Override
    public String toString() {
        return "FileGroup ${prefix} ${filesInGroup.size()}:${filesInGroup}"
    }

}
