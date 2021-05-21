package com.example.musicapplication.domain

class Artist {
    var id = 0
        set(id) {
            require(id >= 0) { "Artist id must be a positive number" }
            field = id
        }
    var name: String? = null
        set(name) {
            require(!(name == null || name.isEmpty())) { "Artist name must not be empty" }
            field = name
        }
    var musicGenre: String? = null
        set(musicGenre) {
            require(!(musicGenre == null || musicGenre.isEmpty())) { "Artist music genre must not be empty" }
            field = musicGenre
        }

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    constructor(name: String?, musicGenre: String?) {
        this.name = name
        this.musicGenre = musicGenre
    }

    override fun toString(): String {
        return name!!
    }
}