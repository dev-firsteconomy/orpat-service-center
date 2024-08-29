package com.orpatservice.app.ui.leads.technician.section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orpatservice.app.R
import com.orpatservice.app.databinding.OpenCameraBottomBinding
import com.orpatservice.app.databinding.OpenCameraBottomSheetBinding
import com.orpatservice.app.ui.admin.technician.CAMERA
import com.orpatservice.app.ui.admin.technician.CANCEL
import com.orpatservice.app.ui.admin.technician.CameraBottomSheetDialogFragment
import com.orpatservice.app.ui.admin.technician.GALLERY

const val CAMERA = "CAMERA"
//const val GALLERY = "GALLERY"
const val CANCEL = "CANCEL"
class CameraBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {
    var bottomSheetItemClick: BottomSheetItemClick? = null

    private lateinit var binding: OpenCameraBottomBinding

    fun newInstance() = CameraBottomDialogFragment().apply {
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
            DataBindingUtil.inflate(inflater, R.layout.open_camera_bottom, container, false)
        binding.tvCamera.setOnClickListener(this)
       // binding.tvGallery.setOnClickListener(this)
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
          /*  R.id.tv_gallery -> {
                bottomSheetItemClick?.bottomSheetItemClick(GALLERY)
            }*/
            R.id.tv_cancel -> {
                bottomSheetItemClick?.bottomSheetItemClick(CANCEL)
            }
        }
        dismiss()

    }
}