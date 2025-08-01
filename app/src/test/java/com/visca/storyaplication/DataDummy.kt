package com.visca.storyaplication

import com.visca.storyaplication.Data.Response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = "story-Alzaskshfhfegfe",
                photoUrl = "https://story-api.dicoding.dev/images/stories/_-zhksH.jpg",
                createdAt = "2023-15-28T00:00:00.176Z",
                name = "Visca",
                description = "Ini Deskripsi",
                lat = (23.9898).toDouble(),
                lon = (7.0202).toDouble(),
            )
            items.add(story)
        }
        return items
    }
}