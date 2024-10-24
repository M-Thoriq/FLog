package com.thoriq.flog.repository

import com.thoriq.flog.R
import com.thoriq.flog.data.RecentCatch

class RecentCatchRepository {
    fun getAllData(): List<RecentCatch>{
        return listOf(
            RecentCatch(
                image = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349
            ),
            RecentCatch(
                image = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349
            ),
            RecentCatch(
                image = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349
            ),
            RecentCatch(
                image = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349
            ),
            RecentCatch(
                image = R.drawable.pngtree_clown_fish_marine_fish_decoration_png_image_4278349
            )
        )
    }
}