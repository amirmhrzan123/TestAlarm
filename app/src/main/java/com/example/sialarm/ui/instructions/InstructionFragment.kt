package com.example.sialarm.ui.instructions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.sialarm.BR
import com.example.sialarm.R
import com.example.sialarm.base.BaseFragment
import com.example.sialarm.databinding.FragmentSlideLeftRightBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class InstructionFragment :DialogFragment(){



    companion object{
        val TAG = InstructionFragment::class.java!!.getSimpleName()


        fun newInstance(quoteRequestMode: Int): InstructionFragment {
            val fragment = InstructionFragment()
            val args = Bundle()
            args.putInt(TAG, quoteRequestMode)
            fragment.setArguments(args)
            return fragment
        }

     }




    private val instructionViewModel : InstructionViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_slide_left_right

    override fun getViewModel(): InstructionViewModel = instructionViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}