package com.example.sampleapp.views.settingsfrag

import com.example.sampleapp.R
import com.example.sampleapp.databinding.FragmentSettingsBinding
import com.example.sampleapp.utils.SessionManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private var mainView: View? = null
    private var isOpen=false

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupBinding(inflater, parent)
       setRadios()
        setClickListeners()
        return mainView
    }

    private fun setRadios() {
        when(SessionManager.getRadio()){
            "standard"->binding.standard.isChecked=true
            "metric"->binding.metric.isChecked=true
            "imperial"->binding.imperial.isChecked=true
        }
    }

    private fun setClickListeners() {
        binding.reset.setOnClickListener {
            SessionManager.setReset(true)
            Toast.makeText(activity,"Bookmarks Reset",Toast.LENGTH_SHORT).show()
        }
        binding.itemArea.setOnClickListener{
            if(!isOpen){
                binding.radioGroup.visibility=View.VISIBLE
                isOpen=true

            }
            else{
                binding.radioGroup.visibility=View.GONE
                isOpen=false
            }
        }
        binding.radioGroup.setOnCheckedChangeListener(object:RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
               val checkedItem:RadioButton=activity?.findViewById(p1) as RadioButton
                SessionManager.setRadio(checkedItem.text.toString().toLowerCase(Locale.getDefault()))
            }

        })
    }

    private fun setupBinding(inflater: LayoutInflater, parent: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, parent, false)
        binding.lifecycleOwner = this
        mainView = binding.root

    }

    override fun onResume() {
        super.onResume()
        binding.radioGroup.visibility=View.GONE
        isOpen=false
        setRadios()
    }
}