package pet.articles.tool.exception

class DuplicateUserException(val fieldName: String, message: String) : Exception(message)