package com.example.littlelemon

object ImageUtils {
    fun formatImageUrl(url: String): String {
        return url
            .replace("github.com", "raw.githubusercontent.com")
            .replace("/blob/", "/")
            .replace("?raw=true", "")
    }
}