package com.example.androidmapspolylines.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import com.example.androidmapspolylines.R
import com.example.androidmapspolylines.Models.Ride
import com.example.androidmapspolylines.UI.Interfaces.BottomSheetInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomFragmentSpecialReq : BottomSheetDialogFragment(){

    private var callBackListener: BottomSheetInterface?=null

    private var btnInitTrip:Button?=null
    private var ckbkids:CheckBox?=null
    private var ckbPets:CheckBox?=null
    private var ckbLuggage:CheckBox?=null

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
        val view = inflater.inflate(R.layout.bottom_fragment_special_req, container, false)

        ckbkids = view.findViewById(R.id.ckb_kids) as? CheckBox
        ckbPets = view.findViewById(R.id.ckb_pets) as? CheckBox
        ckbLuggage = view.findViewById(R.id.ckb_luggage) as? CheckBox
        btnInitTrip = view.findViewById(R.id.ib_init_trip) as? Button


        var ride = Ride()

        ckbkids!!.setOnCheckedChangeListener { buttonView, isChecked -> ride.isForKids = isChecked }
        ckbPets!!.setOnCheckedChangeListener { buttonView, isChecked -> ride.isForPets = isChecked }
        ckbLuggage!!.setOnCheckedChangeListener { buttonView, isChecked -> ride.isForLuggage = isChecked }

        btnInitTrip!!.setOnClickListener{
            callBackListener!!.callbackSpqcialReq(ride)
            dismiss()
        }

        return view
    }


}