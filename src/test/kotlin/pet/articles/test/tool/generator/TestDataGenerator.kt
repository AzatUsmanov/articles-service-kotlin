package pet.articles.test.tool.generator

interface TestDataGenerator<T> {

    fun generateSavedData(): T

    fun generateUnsavedData(): T

    fun generateInvalidData(): T

    fun generateSavedData(dataSize: Int): List<T> =
        generateSequence(::generateSavedData).take(dataSize).toList()

    fun generateUnsavedData(dataSize: Int): List<T> =
        generateSequence(::generateUnsavedData).take(dataSize).toList()
}