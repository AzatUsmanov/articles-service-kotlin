package pet.articles.repository

import jakarta.validation.constraints.NotEmpty

import pet.articles.model.dto.Article

interface ArticleRepository {

    fun save(article: Article): Article

    fun updateById(article: Article, id: Int): Article

    fun deleteById(id: Int)

    fun findById(id: Int): Article?

    fun findByIds(@NotEmpty ids: List<Int>): List<Article>

    fun findAll(): List<Article>

    fun existsById(id: Int): Boolean
}