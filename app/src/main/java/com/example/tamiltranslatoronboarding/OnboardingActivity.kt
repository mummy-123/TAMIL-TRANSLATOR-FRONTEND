package com.example.tamiltranslatoronboarding

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnNext: Button
    private lateinit var btnSkip: Button
    private lateinit var pagerIndicator: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        pagerIndicator = findViewById(R.id.pagerIndicator)

        adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        setupIndicators()
        setCurrentIndicator(0)

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
            }
        })

        btnNext.setOnClickListener {
            val cur = viewPager.currentItem
            if (cur + 1 < adapter.itemCount) {
                viewPager.currentItem = cur + 1
            } else {
                // when Next on last, go ReadyActivity
                goReady()
            }
        }

        btnSkip.setOnClickListener {
            goReady()
        }
    }

    private fun goReady(){
        startActivity(Intent(this, ReadyActivity::class.java))
        finish()
    }

    private fun setupIndicators() {
        val count = adapter.itemCount
        val params = LinearLayout.LayoutParams(12, 12)
        params.setMargins(8, 0, 8, 0)
        for (i in 0 until count) {
            val dot = View(this)
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.OVAL
            drawable.setSize(12, 12)
            drawable.setColor(resources.getColor(R.color.text_secondary))
            dot.background = drawable
            dot.layoutParams = params
            pagerIndicator.addView(dot)
        }
    }

    private fun setCurrentIndicator(index: Int){
        for (i in 0 until pagerIndicator.childCount){
            val v = pagerIndicator.getChildAt(i)
            val drawable = v.background as GradientDrawable
            if (i == index) {
                drawable.setColor(resources.getColor(R.color.accent_blue))
                drawable.setSize(16, 16)
            } else {
                drawable.setColor(resources.getColor(R.color.text_secondary))
                drawable.setSize(12, 12)
            }
            v.background = drawable
        }
    }
}