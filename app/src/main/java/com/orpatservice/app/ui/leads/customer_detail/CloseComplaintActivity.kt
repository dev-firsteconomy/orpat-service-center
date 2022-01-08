package com.orpatservice.app.ui.leads.customer_detail

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.orpatservice.app.databinding.ActivityCloseComplaintBinding
import com.orpatservice.app.ui.admin.technician.TechniciansViewModel


class CloseComplaintActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding : ActivityCloseComplaintBinding
    private lateinit var viewModel: TechniciansViewModel


    private val TRIGGER_AUTO_COMPLETE = 100
    private val AUTO_COMPLETE_DELAY: Long = 300
    private var handler: Handler? = null
    private val autoSuggestAdapter: AutoSuggestAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloseComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TechniciansViewModel::class.java]

        val fewRandomSuggestedText = arrayOf(
            "a", "ant", "apple", "asp", "android", "animation", "adobe",
            "chrome", "chromium", "firefox", "freeware", "fedora"
        )

        val randomArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fewRandomSuggestedText)
        binding.includedContent.mtvParts.setAdapter(randomArrayAdapter)

        binding.includedContent.mtvParts.threshold = 2
        binding.includedContent.mtvParts.onItemClickListener = this

        binding.includedContent.mtvParts.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        binding.includedContent.mtvParts.addTextChangedListener(textWatcher)


    }

    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }





    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }
}