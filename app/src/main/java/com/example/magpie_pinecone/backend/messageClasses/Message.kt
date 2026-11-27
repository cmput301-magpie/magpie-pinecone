package com.example.magpie_pinecone.backend.messageClasses

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.ownerClasses.Owner
import java.util.Date

/**
 * Stores the data associated with a message.
 *
 * @property id The unique id of this message
 * @property text The message content.
 * @property sentAt The date this message was sent.
 * @property sender The owner that sent this message.
 * @property recipient The entrant that this message was sent to.
 * @property campsite The campsite this message was sent for.
 * @constructor Creates a Message class
 */


class Message(
    var id: String = "",
    var text: String = "",
    var sentAt: Date? = null,
    var sender: Owner = Owner(),
    var recipient: Entrant = Entrant(),
    var campsite: Campsite = Campsite()
) {
}
