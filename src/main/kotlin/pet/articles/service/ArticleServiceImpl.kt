package pet.articles.service

import org.springframework.stereotype.Service

import pet.articles.model.dto.Article
import pet.articles.model.dto.AuthorshipOfArticle
import pet.articles.repository.ArticleRepository
import pet.articles.repository.AuthorshipOfArticleRepository
import pet.articles.repository.UserRepository

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val authorshipOfArticleRepository: AuthorshipOfArticleRepository
) : ArticleService {

    override fun create(article: Article, authorIds: List<Int>): Article {
        val savedArticle: Article = articleRepository.save(article)
        saveAuthorship(savedArticle, authorIds)
        return savedArticle
    }

    override fun updateById(article: Article, id: Int): Article {
        if (!existsById(id)) {
            throw NoSuchElementException("Attempt to update article by non existent id = $id")
        }
        return articleRepository.updateById(article, id)
    }

    override fun deleteById(id: Int) = articleRepository.deleteById(id)

    override fun findById(id: Int): Article? = articleRepository.findById(id)

    override fun findArticlesByAuthorId(authorId: Int): List<Article> {
        val articleIds: List<Int> = authorshipOfArticleRepository.findArticleIdsByAuthorId(authorId)
        return findByIds(articleIds)
    }

    override fun findByIds(articleIds: List<Int>): List<Article> =
        if (articleIds.isNotEmpty()) articleRepository.findByIds(articleIds) else emptyList()

    override fun findAll(): List<Article> = articleRepository.findAll()

    override fun existsById(id: Int): Boolean = articleRepository.existsById(id)

    private fun saveAuthorship(savedArticle: Article, authorIds: List<Int>) {
        val authorshipOfArticles = transformToListOfAuthorshipOfArticle(savedArticle, authorIds)
        authorshipOfArticleRepository.save(authorshipOfArticles)
    }

    private fun transformToListOfAuthorshipOfArticle(
        savedArticle: Article,
        authorIds: List<Int>
    ): List<AuthorshipOfArticle> =
        authorIds.map { AuthorshipOfArticle(it, savedArticle.id!!) }
}