package com.orpatservice.app.ui.leads.technician.section

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orpatservice.app.R
import com.orpatservice.app.base.Callback
import com.orpatservice.app.data.model.requests_leads.*
import com.orpatservice.app.databinding.ItemTechnicianComplaintBinding
import com.orpatservice.app.databinding.TechnicianTaskUpdateBinding
import com.orpatservice.app.databinding.UnderWarrantyPartsAdapterBinding
import com.orpatservice.app.ui.leads.customer_detail.CheckWarrantyList
import com.orpatservice.app.ui.leads.customer_detail.FullScreenImageActivity
import com.orpatservice.app.ui.leads.customer_detail.productListData
import com.orpatservice.app.ui.leads.new_lead_fragment.adapter.GridAdapter
import com.orpatservice.app.ui.leads.technician.response.TechnicianEnquiryImage
import com.orpatservice.app.utils.CommonUtils
import com.orpatservice.app.utils.Constants
import com.orpatservice.app.utils.Utils

class WarrantryPartAdapter(
    private val context :Context,
    private val warrantyList: ArrayList<WarrantryPart>,
    private  val leadImageList: ArrayList<LeadEnquiryImage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var callback: Callback? = null
    var selectedImg: String? = ""
    var alertDialogBuilder: Dialog? = null
    var bind: UnderWarrantyPartsAdapterBinding?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: UnderWarrantyPartsAdapterBinding =
            UnderWarrantyPartsAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return TechnicianViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TechnicianViewHolder -> {
                holder.bind(context, warrantyList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return warrantyList.size
    }

    inner class TechnicianViewHolder(private val binding: UnderWarrantyPartsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var selectedParts: String = ""


        init {
            // binding.btnAssignAlltechnician.setOnClickListener(this)
        }

        fun bind(context: Context, technicianData: WarrantryPart) {

           // bind = binding
            binding.tvWarrantyParts.text =
                technicianData.name

            binding.checkWarrantyParts.setOnClickListener {

                    if (binding.checkWarrantyParts.isChecked) {

                        selectedParts = warrantyList[position].id.toString()

                        val warrantyPart = CheckWarrantyList(adapterPosition,"true")
                        CommonUtils.warrantyListData.add(warrantyPart)
                    }else{

                        //val warrantyPart = CheckWarrantyList(adapterPosition,"false")
                        binding.imgSelectedImage.visibility = INVISIBLE
                        CommonUtils.warrantyListData.removeAt(adapterPosition)
                        CommonUtils.imageData.removeAt(adapterPosition)
                        println("CommonUtils.warrantyListData"+CommonUtils.warrantyListData)
                        println("CommonUtils.warrantyListData"+CommonUtils.imageData)
                    }
            }
            binding.tvUploadImage.setOnClickListener {

                //println("secondImgsecondImg"+secondImg)

                if (!selectedParts.isEmpty()) {
                    showImageList()
                } else {
                    Utils.instance.popupPinUtil(
                        context,
                        "Please select warranty parts!",
                        "",
                        false
                    )
                }
            }
            binding.imgSelectedImage.setOnClickListener {

                if(!CommonUtils.imageData.isEmpty()) {
                   // println("CommonUtils.imageData[0].image_data" + CommonUtils.imageData[adapterPosition].image_data)
                    val pos = CommonUtils.imageData[adapterPosition].image_data
                    goToFullScreenImageActivity(leadImageList[pos].image)
                }
            }
        }

        private fun goToFullScreenImageActivity(uploadImage: String?) {
            val intent = Intent(context, FullScreenImageActivity::class.java)
            intent.putExtra(Constants.IMAGE_URL, uploadImage)
            context.startActivity(intent)
        }

        private fun showImageList() {

            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.layout_grid, null)

            val grid_view = view.findViewById<GridView>(R.id.gridView)
            val popupcloseImg = view.findViewById<ImageView>(R.id.popup_img_close)
            alertDialogBuilder = Dialog(context)
            alertDialogBuilder!!.setContentView(view)

            val gridAdapter =
                GridAdapter(context, leadImageList, itemClickListener = onItemClickListener)
            grid_view.adapter = gridAdapter

           /* grid_view.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->

                    val imageView = view.findViewById<ImageView>(R.id.imageView)
                    val imageView_hide = view.findViewById<ImageView>(R.id.imageView_hide)


                    val imagePos = ImageData(position, "true")
                    CommonUtils.imageData.add(imagePos)

                    alertDialogBuilder.dismiss()

                    selectedImg = leadImageList[position].image

                    Glide.with(context)
                        .load(leadImageList[position].image)
                        .placeholder(R.color.gray)
                        .into(binding.imgSelectedImage)

                    val warrantyPartData = productListData(
                        warrantyList[position].id.toString(),
                        leadImageList[position].id.toString()
                    )
                    CommonUtils.productListData.add(warrantyPartData)
                }*/

            alertDialogBuilder!!.show()
            alertDialogBuilder!!.setCanceledOnTouchOutside(true);
            popupcloseImg.setOnClickListener {
                alertDialogBuilder!!.dismiss()
            }
        }


        override fun onClick(p0: View?) {

        }

        private val onItemClickListener: (Int, View) -> Unit =
            { position, view ->


                val imagePos = ImageData(position, "true")
                CommonUtils.imageData.add(imagePos)

                alertDialogBuilder?.dismiss()

                selectedImg = leadImageList[position].image
                binding.imgSelectedImage.visibility = VISIBLE
                Glide.with(context)
                    .load(leadImageList[position].image)
                    .placeholder(R.color.gray)
                    .into(binding.imgSelectedImage)


                val warrantyPartData = productListData(
                    warrantyList[position].id.toString(),
                    leadImageList[position].id.toString()
                )
                CommonUtils.productListData.add(warrantyPartData)
            }
    }
}

