package com.orpatservice.app.ui.addtechnician

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orpatservice.app.R
import com.orpatservice.app.databinding.OpenCameraBottomSheetBinding

const val CAMERA = "CAMERA"
const val GALLERY = "GALLERY"
const val CANCEL = "CANCEL"
class CameraBottomSheetDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    var bottomSheetItemClick: BottomSheetItemClick? = null

    private lateinit var binding: OpenCameraBottomSheetBinding

    fun newInstance() = CameraBottomSheetDialogFragment().apply {
        arguments = Bundle().apply {
            // putString(MyDialogFragment.ARG_URL, url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.open_camera_bottom_sheet, container, false)
        binding.tvCamera.setOnClickListener(this)
        binding.tvGallery.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
        return binding.root

    }

    interface BottomSheetItemClick {
        fun bottomSheetItemClick(clickAction: String?)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_camera -> {
                bottomSheetItemClick?.bottomSheetItemClick(CAMERA)
            }
            R.id.tv_gallery -> {
                bottomSheetItemClick?.bottomSheetItemClick(GALLERY)
            }
            R.id.tv_cancel -> {
                bottomSheetItemClick?.bottomSheetItemClick(CANCEL)
            }
        }
        dismiss()

    }
}