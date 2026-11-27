package com.example.magpie_pinecone.backend.messageClasses

/**
 * Stores a log of messages
 *
 * @property messages The log of messages
 */
class MessageLog(
    private var messages: MutableList<Message> = mutableListOf<Message>()
) {

    /**
     *  Gets a log of messages
     */
    fun getMessages(): List<Message> {
        TODO("Add db functionality to grab messages")
    }
}