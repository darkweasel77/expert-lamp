package org.example.controller

import org.example.service.LocalCache
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class Controller(val cache: LocalCache) {
    @GetMapping("v1/get/{key}")
    @ResponseStatus(HttpStatus.OK)
    fun getValueForKey(
        @PathVariable("key") key: Int
    ): String {
        return cache.getValueForKey(key).value
    }
}
