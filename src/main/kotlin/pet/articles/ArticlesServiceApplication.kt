package pet.articles

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArticlesServiceApplication

fun main(args: Array<String>) {
	runApplication<ArticlesServiceApplication>(*args)
}
