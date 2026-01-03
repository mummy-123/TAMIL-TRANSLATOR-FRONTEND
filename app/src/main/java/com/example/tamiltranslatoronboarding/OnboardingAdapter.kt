package com.example.tamiltranslatoronboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> IntroFragment.newInstance(
                iconRes = R.drawable.ic_book,
                title = "Why This App?",
                subtitle = "Bridge the language gap between English and Tamil with instant, accurate translations powered by advanced AI technology"
            )
            1 -> IntroFragment.newInstance(
                iconRes = R.drawable.ic_star,
                title = "Key Features",
                subtitle = "Text, voice, and camera translation\nOffline mode\nLearning tools and vocabulary builder"
            )
            else -> IntroFragment.newInstance(
                iconRes = R.drawable.ic_brain,
                title = "AI Assist",
                subtitle = "Get intelligent suggestions for grammar, context, pronunciation, and more. Your personal language learning assistant"
            )
        }
    }
}
