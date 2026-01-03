package com.example.tamiltranslatoronboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
private const val ARG_ICON = "arg_icon"
private const val ARG_TITLE = "arg_title"
private const val ARG_SUB = "arg_sub"

class IntroFragment : Fragment(
) {

    companion object {
        fun newInstance(iconRes: Int, title: String, subtitle: String): IntroFragment {
            val f = IntroFragment()
            val args = Bundle()
            args.putInt(ARG_ICON, iconRes)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_SUB, subtitle)
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_intro, container, false)
        val icon = v.findViewById<ImageView>(R.id.ivIcon)
        val title = v.findViewById<TextView>(R.id.tvTitle)
        val sub = v.findViewById<TextView>(R.id.tvSubtitle)

        arguments?.let {
            icon.setImageResource(it.getInt(ARG_ICON))
            title.text = it.getString(ARG_TITLE)
            sub.text = it.getString(ARG_SUB)
        }
        return v
    }
}
