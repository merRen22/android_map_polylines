package com.example.androidmapspolylines.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.androidmapspolylines.R
import com.example.androidmapspolylines.UI.Interfaces.BottomSheetInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragmentUserPreferences : BottomSheetDialogFragment(){

    private var callBackListener: BottomSheetInterface?=null

    private var ibPrivateMode:ImageButton?=null
    private var ibPoolMode:ImageButton?=null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is BottomSheetInterface)
        {
            callBackListener = activity as BottomSheetInterface
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_fragment_user_preferences, container, false)

        ibPrivateMode = view.findViewById(R.id.ib_private_mode) as? ImageButton
        ibPoolMode = view.findViewById(R.id.ib_pool_mode) as? ImageButton



        ibPrivateMode!!.setOnClickListener{
            callBackListener!!.callbackMethod("private")
            dismiss()
        }

        ibPoolMode!!.setOnClickListener{
            callBackListener!!.callbackMethod("pool")
            dismiss()
        }

        return view
    }


}