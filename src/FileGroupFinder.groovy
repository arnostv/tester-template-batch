class FileGroupFinder {
    List<String>  excludedNames
    File location;

    FileGroupFinder(File location) {
        this(location, Collections.emptyList())
    }

    FileGroupFinder(File location, List<String> excludedNames) {
        this.location = location
        this.excludedNames = excludedNames
    }

    Collection<FileGroup> findGroups() {
        def allFiles = location.listFiles().toList()

        def files = allFiles.findAll {
            def name = it.name
            def containing = excludedNames.contains(name)
            def filtered = !containing
            filtered
        }

        def groups = files.groupBy{ file ->
            def dot = file.name.indexOf(".")
            dot != -1 ? file.name.substring(0,dot) : file
        }

        groups.collect { group ->
            File[] f = group.value.toArray()
            new FileGroup(f)
        }
    }
}
