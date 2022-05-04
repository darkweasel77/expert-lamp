package org.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

//val om = ObjectMapper()
//        .registerModule(
//            KotlinModule.Builder()
//                .withReflectionCacheSize(512)
//                .configure(KotlinFeature.NullToEmptyCollection, false)
//                .configure(KotlinFeature.NullToEmptyMap, false)
//                .configure(KotlinFeature.NullIsSameAsDefault, false)
//                .configure(KotlinFeature.SingletonSupport, false)
//                .configure(KotlinFeature.StrictNullChecks, false)
//                .build()
//        )
//        .activateDefaultTyping(
//            BasicPolymorphicTypeValidator.builder()
//            .allowIfBaseType(Any::class.java)
//            .build(), ObjectMapper.DefaultTyping.EVERYTHING)

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
