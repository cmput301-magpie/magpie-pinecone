package com.example.magpie_pinecone.backend.reviewClasses

import java.util.Date

/**
 * A review of a campsite: a star rating plus a written comment.
 * There is one review per entrant per campsite, and its ID is "campsiteID_entrantID",
 * so posting again edits the existing review instead of adding a second one.
 *
 * @property ID The id of the review.
 * @property campsiteID The id of the campsite being reviewed.
 * @property entrantID The id of the entrant who wrote the review.
 * @property entrantName The reviewer's name when the review was posted.
 * @property rating The star rating, from 1 to 5.
 * @property comment The written part of the review.
 * @property createdAt When the review was last posted or edited.
 */
class Review (
    var ID: String = "",
    var campsiteID: String = "",
    var entrantID: String = "",
    var entrantName: String = "",
    var rating: Int = 0,
    var comment: String = "",
    var createdAt: Date = Date()
)