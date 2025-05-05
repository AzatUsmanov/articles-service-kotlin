package pet.articles.aspect.log

import mu.KotlinLogging

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect

import org.springframework.stereotype.Component

import pet.articles.model.dto.Article

@Aspect
@Component
class ArticleLogAspect {
    private val log = KotlinLogging.logger {}

    @AfterReturning(
        pointcut = "execution(* pet.articles.service.ArticleServiceImpl.create(..))",
        returning = "createdArticle"
    )
    fun logArticleCreation(createdArticle: Article) {
        log.info { "Article was created successfully $createdArticle" }
    }

    @AfterReturning(
        pointcut = "execution(* pet.articles.service.ArticleServiceImpl.updateById(..))",
        returning = "updatedArticle"
    )
    fun logArticleUpdate(updatedArticle: Article) {
        log.info { "Article with id = ${updatedArticle.id} was updated successfully $updatedArticle" }
    }

    @AfterReturning("execution(* pet.articles.service.ArticleServiceImpl.deleteById(..)) && args(id)")
    fun logArticleDeletion(id: Int) {
        log.info { "Article with id = $id was deleted successfully" }
    }
}