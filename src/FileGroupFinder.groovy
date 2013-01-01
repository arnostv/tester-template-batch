class FileGroupFinder {
    File location;

    FileGroupFinder(File location) {
        this.location = location
    }

    Collection<FileGroup> findGroups() {
        def files = location.list().toList()

        def groups = files.groupBy{ file ->
            def dot = file.indexOf(".")
            dot != -1 ? file.substring(0,file.indexOf(".")) : file
        }

        groups.collect { group ->
            String[] filesInGroup = group.value.toArray()
            new FileGroup(filesInGroup)
        }
    }
}
