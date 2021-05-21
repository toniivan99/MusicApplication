package com.example.musicapplication.domain

class Song {
    var id = 0
        set(id) {
            require(id >= 0) { "Song id must be a positive number" }
            field = id
        }
    var title: String? = null
        set(title) {
            require(!(title == null || title.isEmpty())) { "Song title must not be empty" }
            field = title
        }
    var releasedDate: String? = null
    var duration = 0
    var artist: Artist? = null
        set(artist) {
            kotlin.requireNotNull(artist) { "Song artist must not be null" }
            field = artist
        }

    constructor() {}
    constructor(title: String?, releasedDate: String?, duration: Int, artist: Artist?) {
        this.title = title
        this.releasedDate = releasedDate
        this.duration = duration
        this.artist = artist
    }

    override fun toString(): String {
        return artist!!.name + "-" + title
    }
}