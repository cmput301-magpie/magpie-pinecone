package com.example.magpie_pinecone.backend.notificationClasses

import com.example.magpie_pinecone.backend.entrantClasses.Entrant
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.ownerClasses.Owner
import java.util.Date

/**
 * Stores the data associated with a notification.
 *
 * @property id The unique id of this notification
 * @property message The message associated with this notification.
 * @property sentAt The date this notification was sent.
 * @property sender The owner that sent this notification.
 * @property recipients The entrants that this notification was sent to.
 * @property lottery The lottery this notification was sent for.
 * @constructor Creates a Notification class
 */


class Notification(
    var id: String = "",
    var message: String = "",
    var sentAt: Date? = null,
    var sender: Owner = Owner(),
    var recipients: MutableList<Entrant> = mutableListOf<Entrant>(),
    var lottery: Lottery = Lottery()
) {
}