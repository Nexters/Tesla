package com.ozcoin.cookiepang.utils

import com.ozcoin.cookiepang.domain.cookiedetail.CookieDetail
import com.ozcoin.cookiepang.domain.cookiehistory.CookieHistory
import com.ozcoin.cookiepang.domain.cookiehistory.CookieHistoryType
import com.ozcoin.cookiepang.domain.feed.CookieCardStyle
import com.ozcoin.cookiepang.domain.feed.Feed
import com.ozcoin.cookiepang.domain.usercategory.UserCategory
import java.util.Random

object DummyUtil {

    fun getUserCategoryList() = DataResult.OnSuccess(
        listOf(
            UserCategory("Free Chat", false),
            UserCategory("Money", false),
            UserCategory("Friend", false),
            UserCategory("Hobby", false),
        )
    )

    fun getFeedList(): DataResult.OnSuccess<List<Feed>> {
        val list = mutableListOf<Feed>()
        val random = Random()

        repeat(100) {
            val feed = Feed(
                isHidden = it % 8 != 0,
                userThumbnailUrl = "",
                userProfileId = "Say My Name#$it",
                createdTimeStamp = "$it min ago",
                question = "My real Secret is too many speaker(#$it)",
                answer = "오늘도 이 비는 그치지 않아. 모두 어디서 흘러 오는 건지. 창 밖으로 출렁이던 헤드라잇 강물도. 갈 곳을 잃은채 울먹이고, 자동응답기의 공허한 시간. 모두 어디로 흘러가는 건지. 기다림은 방 한 구석 잊혀진 화초처럼. 조금씩 시들어 고개 숙여가고.",
                cookieCardStyle = if (it % 3 == 0) CookieCardStyle.BLUE else if (it % 3 == 1) CookieCardStyle.YELLOW else CookieCardStyle.PINK,
                viewCount = it * (random.nextInt(291) + 1),
                hammerPrice = it * (random.nextInt(8) + 1),
            )
            list.add(feed)
        }

        return DataResult.OnSuccess(list.toList())
    }

    fun getCookieDetail(isMine: Boolean, isHidden: Boolean): DataResult<CookieDetail> {
        return DataResult.OnSuccess(
            CookieDetail(
                isMine = isMine,
                userCategory = UserCategory("Free Chat", true),
                viewCount = 423,
                question = "나에게 가장 수치스러운 것은?",
                answer = null,
                hammerPrice = 24,
                cookieCardStyle = CookieCardStyle.YELLOW,
                collectorThumbnailUrl = "",
                collectorName = "강동구 호랑이3333333",
                creatorThumbnailUrl = "",
                creatorName = "상일동 치타",
                contractAddress = "0xesf232424124214124214",
                tokenAddress = "0xesf232424124214124214",
                cookieHistory = listOf(
                    CookieHistory(
                        CookieHistoryType.PURCHASE,
                        "‘강동구 호랑이' 님이 ‘Q.내가 여자친구가 있을까'를 망치 32개로 수정했습니다.",
                        "2022-02-22 20:00"
                    ),
                    CookieHistory(
                        CookieHistoryType.MODIFY,
                        "‘강동구 호랑이' 님이 ‘Q.내가 여자친구가 있을까'를 망치 34개로 깠습니다.",
                        "2022-10-32 20:00"
                    ),
                    CookieHistory(
                        CookieHistoryType.CREATE,
                        "‘상일동 치타' 님이 ‘Q.내가 여자친구가 있을까'를 망치 34개로 만들었습니다.",
                        "2022-10-32 20:00"
                    )
                ),
                isHidden = isHidden
            )
        )
    }
}
