package com.valyriapps.maggificient.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.valyriapps.maggificient.R
import kotlinx.android.synthetic.main.fragment_base_routing.*

class RecordRoutingFragment : Fragment() {
    companion object {
        fun newInstance() = RecordRoutingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_routing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = RecordCaptureFragment.newInstance()
        if (savedInstanceState == null) {
            this.activity?.apply {
                supportFragmentManager.beginTransaction()
                    .replace(this.routingContainer.id, fragment, fragment.javaClass.simpleName)
                    .commit()
            }
        }
    }
}