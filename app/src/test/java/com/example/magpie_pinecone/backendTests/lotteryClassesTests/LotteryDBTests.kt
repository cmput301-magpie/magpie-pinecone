package com.example.magpie_pinecone.backendTests.lotteryClassesTests

import com.example.magpie_pinecone.backend.campsiteClasses.Campsite
import com.example.magpie_pinecone.backend.lotteryClasses.Lottery
import com.example.magpie_pinecone.backend.lotteryClasses.pickLotteryForCampsite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class LotteryDBTests {

    private val now = Date(1_800_000_000_000)   // a fixed "now" so the test always gives the same result
    private fun daysFromNow(days: Long) = Date(now.time + days * 86_400_000L)

    private fun testLottery (
        id: String,
        campsiteID: String,
        registrationStart: Date? = null,
        registrationEnd: Date? = null
    ) = Lottery(
        id = id,
        campsites = mutableListOf(Campsite(ID = campsiteID)),
        registrationStartDate = registrationStart,
        registrationEndDate = registrationEnd
    )

    @Test
    fun pickLottery_choosesLotteryThatIncludesTheCampsite() {
        val other = testLottery("other", "site2")
        val wanted = testLottery("wanted", "site1")

        // The other lottery comes first, but it is for a different campsite
        assertEquals("wanted", pickLotteryForCampsite(listOf(other, wanted), "site1", now)?.id)
    }

    @Test
    fun pickLottery_withNoLotteryForCampsite_returnsNull() {
        val lotteries = listOf(testLottery("a", "site2"), testLottery("b", "site3"))

        assertNull(pickLotteryForCampsite(lotteries, "site1", now))
        assertNull(pickLotteryForCampsite(emptyList(), "site1", now))
    }

    @Test
    fun pickLottery_prefersTheOneWithOpenRegistration() {
        val old = testLottery("old", "site1", daysFromNow(-20), daysFromNow(-10))
        val future = testLottery("future", "site1", daysFromNow(10), daysFromNow(20))
        val open = testLottery("open", "site1", daysFromNow(-1), daysFromNow(5))

        assertEquals("open", pickLotteryForCampsite(listOf(old, future, open), "site1", now)?.id)
    }

    @Test
    fun pickLottery_withNoOpenRegistration_fallsBackToFirstMatch() {
        val old = testLottery("old", "site1", daysFromNow(-20), daysFromNow(-10))
        val future = testLottery("future", "site1", daysFromNow(10), daysFromNow(20))

        assertEquals("old", pickLotteryForCampsite(listOf(old, future), "site1", now)?.id)
    }
}