package pet.articles.controller

import jakarta.validation.Valid

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import pet.articles.model.dto.Article
import pet.articles.model.dto.payload.NewArticlePayload
import pet.articles.model.dto.payload.UpdateArticlePayload
import pet.articles.service.ArticleService

@RestController
@RequestMapping(
    path = ["\${api.paths.articles}"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class ArticleController(
    private val articleService: ArticleService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid articlePayload: NewArticlePayload): Article =
        articleService.create(articlePayload.toArticle(), articlePayload.authorIds)

    @PatchMapping("/{id}")
    fun updateById(
        @RequestBody @Valid articlePayload: UpdateArticlePayload,
        @PathVariable id: Int
    ): Article = articleService.updateById(articlePayload.toArticle(), id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: Int) = articleService.deleteById(id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Article> =
        articleService.findById(id)?.let { article -> ResponseEntity.ok(article) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/authorship/{authorId}")
    fun findAuthorsOfArticle(@PathVariable authorId: Int): List<Article> =
        articleService.findArticlesByAuthorId(authorId)

    @GetMapping
    fun getAll(): List<Article> = articleService.findAll()
}

